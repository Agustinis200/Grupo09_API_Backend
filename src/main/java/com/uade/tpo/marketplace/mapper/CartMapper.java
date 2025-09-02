package com.uade.tpo.marketplace.mapper;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.controllers.cart.CartResponse;
import com.uade.tpo.marketplace.controllers.cart.ItemCartResponse;
import com.uade.tpo.marketplace.entity.ItemCart;
import com.uade.tpo.marketplace.service.cart.CartPriceCalculator;
import com.uade.tpo.marketplace.entity.Cart;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart, CartPriceCalculator calculator) {
        if (cart == null) {
            return new CartResponse();
        }
        
        return CartResponse.builder()
            .items(toItemResponseList(cart.getItems()))
            .totalPrice(calculator.total(cart))
            .build();
    }

    public ItemCartResponse toItemResponse(ItemCart item) {
        if (item == null) return null;
        
        return ItemCartResponse.builder()
            .productName(item.getProduct() != null ? item.getProduct().getName() : null)
            .quantity(item.getCount())
            .build();
    }

    public List<ItemCartResponse> toItemResponseList(List<ItemCart> items) {
        if (items == null) return new ArrayList<>();
        
        return items.stream()
            .map(this::toItemResponse)
            .collect(Collectors.toList());
    }
}
