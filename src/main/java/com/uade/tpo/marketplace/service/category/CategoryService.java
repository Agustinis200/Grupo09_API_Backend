package com.uade.tpo.marketplace.service.category;

import java.util.List;

import com.uade.tpo.marketplace.controllers.category.CategoryResponse;
import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exception.CategoryDuplicateException;
import com.uade.tpo.marketplace.exception.CategoryNotFoundException;

public interface CategoryService {
    public List<CategoryResponse> getAllCategories();

    public CategoryResponse getCategoryById(Long categoryById) throws CategoryNotFoundException ;

    public CategoryResponse createCategory(String name) throws CategoryDuplicateException;

    public CategoryResponse updateCategory(Long categoryById, String name) throws CategoryNotFoundException;

    public void deleteCategory(Long categoryById) throws CategoryNotFoundException;
    Category createCategoryProduct(String name) ;
}
