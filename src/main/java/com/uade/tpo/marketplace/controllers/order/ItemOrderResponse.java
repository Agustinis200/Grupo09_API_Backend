package com.uade.tpo.marketplace.controllers.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemOrderResponse {
    private long id;
    private int count;
    private String productName;
}
