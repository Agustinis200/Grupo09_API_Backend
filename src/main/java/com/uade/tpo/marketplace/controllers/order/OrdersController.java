package com.uade.tpo.marketplace.controllers.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.service.order.OrderService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/admin/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/admin/{userId}/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long userId, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(userId, orderId));
    }

    @GetMapping("/user/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@AuthenticationPrincipal User user, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(user.getId(), orderId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(user.getId()));
    }
    
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest, user));
    }
    

    @PutMapping("/user/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@RequestBody OrderRequest orderRequest, @AuthenticationPrincipal(expression = "id") Long userId, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.updateOrder(orderRequest, userId, orderId));
    }
}
