package com.uade.tpo.marketplace.controllers.product;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Double discount; // Multiplicador de precio (1.0 = sin descuento, 0.5 = 50% descuento)
    private int stock;
    private String category;
}
