package com.uade.tpo.marketplace.service.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.controllers.category.CategoryResponse;
import com.uade.tpo.marketplace.entity.Category;


public interface CategoryService {
    public Page<CategoryResponse> getAllCategories(Pageable pageable);

    public CategoryResponse getCategoryById(Long categoryById);

    public CategoryResponse createCategory(String name) ;

    public CategoryResponse updateCategory(Long categoryById, String name);

    public void deleteCategory(Long categoryById);
    Category createCategoryProduct(String name) ;
}
