package com.uade.tpo.marketplace.controllers.category;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private String name;
    private Long id;
}
