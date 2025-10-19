package com.uade.tpo.marketplace.controllers.product;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Integer stock;
    private String seller;
    private Double discount;
    private String imageFile;
}
