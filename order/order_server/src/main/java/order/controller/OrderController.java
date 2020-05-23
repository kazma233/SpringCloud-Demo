package order.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import order.dto.OrderDTO;
import order.dto.ResultDTO;
import order.entity.OrderDetail;
import order.enums.ResultEnum;
import order.exception.OrderException;
import order.form.OrderForm;
import order.service.OrderService;
import order.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 参数检查
     * 查询商品信息 (通过商品服务)
     * 计算总价
     * 扣库存 (通过商品服务)
     * 订单入库
     */
//    @HystrixCommand(fallbackMethod = "breakHandle", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
//    })
    @PostMapping("/create")
    public ResultDTO create(@Valid @RequestBody OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[创建订单]参数错误, orderForm={}", orderForm);
            throw new OrderException(bindingResult.getFieldError().getDefaultMessage(), ResultEnum.PARAM_ERROR.getCode());
        }

        // orderForm -> orderDto
        OrderDTO orderDTO = convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("购物车信息为空");
            throw new OrderException(ResultEnum.ITEM_EMPTY);
        }

        OrderDTO result = orderService.create(orderDTO);
        Map<String, Object> orderIdMap = new HashMap<>();
        orderIdMap.put("orderId", result.getOrderId());

        return ResultUtils.success(orderIdMap);
    }

    private ResultDTO breakHandle(OrderForm orderForm, BindingResult bindingResult) {
        return ResultUtils.error();
    }

    // 完结订单
    @PostMapping("/finish")
    public ResultDTO<OrderDTO> finish(@RequestParam String orderId) {
        return ResultUtils.success(orderService.finish(orderId));
    }

    private OrderDTO convert(OrderForm orderForm) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerPhone(orderForm.getPhone());

        List<OrderDetail> orderDetails = new ArrayList<>();

        Gson gson = new Gson();

        try {
            orderDetails = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            log.error("items 转换失败 item={}, exception={}", orderForm.getItems(), e.getLocalizedMessage());
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }

        orderDTO.setOrderDetailList(orderDetails);

        return orderDTO;
    }
}
