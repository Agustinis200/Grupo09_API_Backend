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
    private String productName;
    private int quantity;
}
