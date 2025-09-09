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
import com.uade.tpo.marketplace.exception.order.*;
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
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        if (orderRepository.findByUserId(userId).isEmpty()) {
            throw new OrderNotFoundException("No se encontraron órdenes para el usuario con ID: " + userId);
        }
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long userId, Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new OrderNotFoundException("No se encontró la orden con ID: " + orderId + " para el usuario con ID: " + userId));
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, User user) {
        // Verificar que el carrito exista y no esté vacío
        Optional<Cart> cartOpt = cartRepository.findByUserId(user.getId());
        if (cartOpt.isEmpty()) {
            throw new CartIsEmptyException("El carrito está vacío o no existe");
        }
        
        // Crear la orden a partir del carrito
        Order order = orderMapper.fromCart(cartOpt.get(), user, orderRequest);
        order = orderRepository.save(order);
        
        // Guardar los items de la orden
        List<ItemOrder> itemOrders = new ArrayList<>();
        for (ItemOrder itemOrder : order.getItems()) {
            itemOrderRepository.save(itemOrder);
            itemOrders.add(itemOrder);
        }
        
        order.setItems(itemOrders);
        
        // Si el estado es PAID, actualizar el stock de productos
        if (order.getStatus() == StateOrder.PAID) {
            for (ItemOrder itemOrder : order.getItems()) {
                productService.updateProductStock(itemOrder.getProduct().getId(), itemOrder.getCount());
            }
        }
        
        // Limpiar el carrito después de crear la orden
        cartService.clearCart(user.getId());
        
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(OrderRequest orderRequest, Long userId, Long orderId) {
        // Buscar la orden y verificar que esté en estado PENDING
        Optional<Order> orderOpt = orderRepository.findByIdAndUserId(orderId, userId);
        if (orderOpt.isEmpty()) {
            throw new OrderNotFoundException("No se encontró la orden con ID: " + orderId + " para el usuario con ID: " + userId);
        }
        
        Order order = orderOpt.get();
        
        // Verificar que la orden esté en estado PENDING
        if (order.getStatus() != StateOrder.PENDING) {
            throw new InvalidOrderStatusException("Solo se pueden actualizar órdenes en estado PENDIENTE. Estado actual: " + order.getStatus());
        }
        
        // Actualizar la dirección si se proporciona
        if (orderRequest.getAddress() != null) {
            order.setAddress(orderRequest.getAddress());
        }

        // Actualizar el estado si se proporciona
        if (orderRequest.getStatus() != null) {
            order.setStatus(orderRequest.getStatus());
        }
        
        // Guardar los cambios
        order = orderRepository.save(order);
        
        // Si el nuevo estado es PAID, actualizar el stock de productos
        if (orderRequest.getStatus() == StateOrder.PAID) {
            for (ItemOrder itemOrder : order.getItems()) {
                productService.updateProductStock(itemOrder.getProduct().getId(), itemOrder.getCount());
            }
        }
        
        return orderMapper.toResponse(order);
    }

}
