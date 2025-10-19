package com.uade.tpo.marketplace.mapper;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.controllers.cart.CartResponse;
import com.uade.tpo.marketplace.controllers.cart.ItemCartResponse;
import com.uade.tpo.marketplace.entity.ItemCart;
import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart) {
        if (cart == null) {
            return new CartResponse();
        }
        
        return CartResponse.builder()
            .items(toItemResponseList(cart.getItems()))
            .totalPrice(calculateTotalPrice(cart))
            .build();
    }

    public ItemCartResponse toItemResponse(ItemCart item) {
        if (item == null) return null;
        
        Product product = item.getProduct();
        if (product == null) return null;
        
        return ItemCartResponse.builder()
            .productId(product.getId())
            .productName(product.getName())
            .productPrice(product.getPrice())
            .productDiscount(product.getDiscount() != null ? product.getDiscount() : 1.0)
            .productImage(product.getImage() != null ? "/images?id=" + product.getImage().getId() : null)
            .quantity(item.getCount())
            .build();
    }

    public List<ItemCartResponse> toItemResponseList(List<ItemCart> items) {
        if (items == null) return new ArrayList<>();
        
        return items.stream()
            .map(this::toItemResponse)
            .collect(Collectors.toList());
    }

    /**
     * Calcula el precio total del carrito aplicando el descuento de cada producto
     * @param cart El carrito de compras
     * @return El precio total con descuentos aplicados
     */
    private double calculateTotalPrice(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return 0.0;
        }
        
        return cart.getItems().stream()
            .mapToDouble(item -> {
                Product product = item.getProduct();
                if (product == null) return 0.0;
                
                double price = product.getPrice();
                double discount = product.getDiscount() != null ? product.getDiscount() : 1.0;
                int quantity = item.getCount();
                
                // Precio final = precio * multiplicador de descuento * cantidad
                return price * discount * quantity;
            })
            .sum();
    }
}
