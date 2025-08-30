package com.uade.tpo.marketplace.controllers.cart;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.service.CartService;

@RestController
@RequestMapping("carts")
public class CartsController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestParam Long userId) {
        Cart cart = cartService.createCart(userId);
        return ResponseEntity.created(URI.create("/carts/" + cart.getId())).body(cart);
    }

    @GetMapping("{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {
        Cart cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("{cartId}/items")
    public ResponseEntity<Cart> addItem(@PathVariable Long cartId, @RequestBody CartItemRequest request) throws Exception {
        Cart cart = cartService.addItem(cartId, request.getProductId(), request.getCount());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("{cartId}/items/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable Long cartId, @PathVariable Long itemId) throws Exception {
        Cart cart = cartService.removeItem(cartId, itemId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("{cartId}/checkout")
    public ResponseEntity<Cart> checkout(@PathVariable Long cartId) throws Exception {
        Cart cart = cartService.checkout(cartId);
        return ResponseEntity.ok(cart);
    }
}