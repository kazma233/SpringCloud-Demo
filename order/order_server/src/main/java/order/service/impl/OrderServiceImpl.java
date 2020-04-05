package order.service.impl;

import com.product.client.ProductClient;
import com.product.common.DecreaseStockInput;
import com.product.common.ProductInfoOutput;
import order.dao.OrderDetailDao;
import order.dao.OrderMasterDao;
import order.dto.OrderDTO;
import order.entity.OrderDetail;
import order.entity.OrderMaster;
import order.enums.OrderStatuEnum;
import order.enums.PayStatuEnum;
import order.enums.ResultEnum;
import order.exception.OrderException;
import order.service.OrderService;
import order.util.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterDao orderMasterDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private ProductClient productClient;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();
        // 查询商品信息 调用商品服务
        List<String> productIds = orderDTO.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        List<ProductInfoOutput> productInfos = productClient.listForOrder(productIds);
        // 计算总价
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            for (ProductInfoOutput productInfo : productInfos) {
                if (productInfo.getProductId().equals(orderDetail.getProductId())) {
                    orderAmount = productInfo.getProductPrice()
                            .multiply(BigDecimal.valueOf(orderDetail.getProductQuantity()))
                            .add(orderAmount);
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());

                    // 订单详情入库
                    orderDetailDao.save(orderDetail);
                }
            }
        }

        // 扣库存
        List<DecreaseStockInput> cartDTOS = orderDTO.getOrderDetailList().stream()
                .map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(cartDTOS);

        // 订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster); // 设置id等
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatuEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatuEnum.WAIT.getCode());
        orderMasterDao.save(orderMaster);

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(String orderId) {
        Optional<OrderMaster> orderMasterOptional = orderMasterDao.findById(orderId);
        if (!orderMasterOptional.isPresent()) {
            throw new OrderException(ResultEnum.ORDER_STATU_ERROR);
        }

        OrderMaster orderMaster = orderMasterOptional.get();
        // 判断订单状态
        if (!orderMaster.getOrderStatus().equals(OrderStatuEnum.NEW.getCode())) { // 不是新订单
            throw new OrderException(ResultEnum.ORDER_STATU_ERROR);
        }

        // 修改订单状态为完结
        orderMaster.setOrderStatus(OrderStatuEnum.FINSHED.getCode());
        orderMasterDao.save(orderMaster);

        OrderDTO orderDTO = new OrderDTO();

        // 查询订单详情
        List<OrderDetail> orderDetails = orderDetailDao.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetails)) {
            throw new OrderException(ResultEnum.ORDER_DETAIL_NOT_EXITS);
        }

        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetails);
        return orderDTO;
    }
}
