package com.uade.tpo.marketplace.service.order;

import java.util.List;

import com.uade.tpo.marketplace.controllers.order.OrderRequest;
import com.uade.tpo.marketplace.controllers.order.OrderResponse;
import com.uade.tpo.marketplace.entity.User;

public interface OrderService {

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getOrdersByUserId(Long userId);

    OrderResponse createOrder(OrderRequest orderRequest, User user);

    OrderResponse updateOrder(OrderRequest orderRequest, Long userId, Long orderId);

    OrderResponse getOrderById(Long userId, Long orderId);

}
    