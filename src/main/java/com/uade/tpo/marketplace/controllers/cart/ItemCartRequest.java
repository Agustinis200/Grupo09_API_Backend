package com.uade.tpo.marketplace.controllers.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCartRequest {
    private long productId;
    private int quantity;
}
