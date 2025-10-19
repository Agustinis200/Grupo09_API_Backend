package com.uade.tpo.marketplace.controllers.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCartResponse {
    private Long productId;
    private String productName;
    private double productPrice;
    private double productDiscount;
    private String productImage;
    private int quantity;
}
