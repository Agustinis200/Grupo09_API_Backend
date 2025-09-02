package com.uade.tpo.marketplace.controllers.order;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String dateTime;
    private String address;
    private List<ItemOrderResponse> items;
    private String status;
    private Double totalPrice;
}
