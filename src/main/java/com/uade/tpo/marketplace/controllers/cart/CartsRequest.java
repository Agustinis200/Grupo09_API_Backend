package com.uade.tpo.marketplace.controllers.cart;

import lombok.Data;

import java.util.List;

@Data
public class CartsRequest {
    private Long userId;
    private List<CartItemRequest> items;
}