package com.uade.tpo.marketplace.controllers.product;

import com.uade.tpo.marketplace.entity.Category;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Category category;
}
