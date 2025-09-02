package com.uade.tpo.marketplace.service.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.controllers.order.OrderRequest;
import com.uade.tpo.marketplace.controllers.order.OrderResponse;
import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.ItemOrder;
import com.uade.tpo.marketplace.entity.Order;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.enums.StateOrder;
import com.uade.tpo.marketplace.mapper.OrderMapper;
import com.uade.tpo.marketplace.repository.CartRepository;
import com.uade.tpo.marketplace.repository.ItemOrderRepository;
import com.uade.tpo.marketplace.repository.OrderRepository;
import com.uade.tpo.marketplace.service.cart.CartService;
import com.uade.tpo.marketplace.service.product.ProductService;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemOrderRepository itemOrderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long userId, Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Order not found for userId: " + userId + " and orderId: " + orderId));
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, User user) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(user.getId());
        if (cartOpt.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío o no existe");
        }
        
        Order order = orderMapper.fromCart(cartOpt.get(), user, orderRequest);
        
        order = orderRepository.save(order);
        
        List<ItemOrder> itemOrders = new ArrayList<>();
        for (ItemOrder itemOrder : order.getItems()) {
            itemOrderRepository.save(itemOrder);
            itemOrders.add(itemOrder);
        }
        
        order.setItems(itemOrders);
        
        if (order.getStatus() == StateOrder.PAID) {
            for (ItemOrder itemOrder : order.getItems()) {
                productService.updateProductStock(itemOrder.getProduct().getId(), itemOrder.getCount());
            }
        }
        
        cartService.clearCart(user.getId());
        
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(OrderRequest orderRequest, Long userId, Long orderId) {
        Optional<Order> orderOpt = orderRepository.findByIdAndUserId(orderId, userId);
        if (orderOpt.isEmpty() || orderOpt.get().getStatus() != StateOrder.PENDING) {
            throw new IllegalArgumentException("No se encontró ninguna orden pendiente");
        }
        
        Order order = orderOpt.get();
        
        if (orderRequest.getAddress() != null) {
            order.setAddress(orderRequest.getAddress());
        }

        if (orderRequest.getStatus() != null) {
            order.setStatus(orderRequest.getStatus());
        }
        
        order = orderRepository.save(order);
        
        if (orderRequest.getStatus() == StateOrder.PAID) {
            for (ItemOrder itemOrder : order.getItems()) {
                productService.updateProductStock(itemOrder.getProduct().getId(), itemOrder.getCount());
            }
        }
        
        return orderMapper.toResponse(order);
    }

}
