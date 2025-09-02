package com.uade.tpo.marketplace.mapper;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.controllers.category.CategoryRequest;
import com.uade.tpo.marketplace.controllers.category.CategoryResponse;
import com.uade.tpo.marketplace.entity.Category;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toEntity(CategoryRequest request) {
        if (request == null) return null;
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    public void copyNonNull(CategoryRequest request, Category entity) {
        if (request == null || entity == null) return;
        if (request.getName() != null) {
            entity.setName(request.getName());
        }
    }
}