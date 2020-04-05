package order.service;


import order.dto.OrderDTO;

public interface OrderService {

    // 创建订单
    public OrderDTO create(OrderDTO orderDTO);

    // 完结订单 只能卖家操作
    public OrderDTO finish(String orderId);

}
