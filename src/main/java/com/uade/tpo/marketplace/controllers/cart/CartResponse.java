package com.uade.tpo.marketplace.controllers.cart;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private List<ItemCartResponse> items;
    private Double totalPrice;
}
