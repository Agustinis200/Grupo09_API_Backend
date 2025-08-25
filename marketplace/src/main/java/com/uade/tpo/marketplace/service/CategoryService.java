package com.uade.tpo.marketplace.service;

import java.util.List;
import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exception.CategoryDuplicateException;
import com.uade.tpo.marketplace.exception.CategoryNotFoundException;

public interface CategoryService {
    public List<Category> getAllCategories();

    public Category getCategoryById(Long categoryById) throws CategoryNotFoundException ;

    public Category createCategory(String name) throws CategoryDuplicateException;

    public Category updateCategory(Long categoryById, String name) throws CategoryNotFoundException;

    public void deleteCategory(Long categoryById) throws CategoryNotFoundException;
}
