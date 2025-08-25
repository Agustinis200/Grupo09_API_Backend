package com.uade.tpo.marketplace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exception.CategoryDuplicateException;
import com.uade.tpo.marketplace.exception.CategoryNotFoundException;
import com.uade.tpo.marketplace.repository.CategoryRepository;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long categoryById) throws CategoryNotFoundException {
        return categoryRepository.findById(categoryById).orElseThrow(() -> new CategoryNotFoundException());
    }

    public Category createCategory(String name) throws CategoryDuplicateException {
        List<Category> newCategory = categoryRepository.findByName(name);
        if (newCategory.isEmpty()) {
            return categoryRepository.save(new Category(name));
        }
        throw new CategoryDuplicateException();

    }

    public Category updateCategory(Long categoryById, String name) throws CategoryNotFoundException {
        if (!categoryRepository.existsById(categoryById)) {
            throw new CategoryNotFoundException();
        }
        Category existingCategory = categoryRepository.findById(categoryById).get();
        existingCategory.setName(name);
        return categoryRepository.save(existingCategory);
        
    }

    public void deleteCategory(Long categoryById) throws CategoryNotFoundException {
        if (!categoryRepository.existsById(categoryById)) {
            throw new CategoryNotFoundException();
        }
        categoryRepository.deleteById(categoryById);
    }

}
