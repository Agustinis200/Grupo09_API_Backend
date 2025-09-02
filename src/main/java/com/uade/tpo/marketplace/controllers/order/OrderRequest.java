package com.uade.tpo.marketplace.controllers.order;

import com.uade.tpo.marketplace.entity.enums.StateOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String address;
    private StateOrder status;
}
