package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.Category;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Category category;
}
