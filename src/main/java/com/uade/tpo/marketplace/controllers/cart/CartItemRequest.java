package com.uade.tpo.marketplace.controllers.cart;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private int count;
}