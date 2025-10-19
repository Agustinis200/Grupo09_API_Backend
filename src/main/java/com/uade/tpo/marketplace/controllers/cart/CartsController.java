package com.uade.tpo.marketplace.controllers.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.service.cart.CartService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("carts")
public class CartsController {
    @Autowired
    private CartService cartService;

    @GetMapping()
    public ResponseEntity<CartResponse> viewCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.viewCart(user.getId()));
    }

    @PostMapping()
    public ResponseEntity<ItemCartResponse> addToCart(@RequestBody ItemCartRequest itemCartRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.addToCart(itemCartRequest, user));
    }

    @PutMapping()
    public ResponseEntity<CartResponse> updateCart(@RequestBody ItemCartRequest itemCartRequest, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.updateCart(itemCartRequest, user.getId()));
    }
    
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long productId, @AuthenticationPrincipal User user) {
        cartService.removeItemFromCart(user.getId(), productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user.getId());
        return ResponseEntity.ok().build();
    }

}