package com.uade.tpo.marketplace.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.controllers.category.CategoryResponse;
import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exception.CategoryDuplicateException;
import com.uade.tpo.marketplace.exception.CategoryNotFoundException;
import com.uade.tpo.marketplace.mapper.CategoryMapper;
import com.uade.tpo.marketplace.repository.CategoryRepository;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long categoryById) throws CategoryNotFoundException {
        return categoryRepository.findById(categoryById)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException());
    }

    public CategoryResponse createCategory(String name) throws CategoryDuplicateException {
        Optional<Category> existingCategory = categoryRepository.findByName(name);

        if (existingCategory.isPresent()) {
            throw new CategoryDuplicateException();
        }
        
        Category newCategory = Category.builder()
                .name(name)
                .build();
        
        Category savedCategory = categoryRepository.save(newCategory);
        
        return categoryMapper.toResponse(savedCategory);
    }

    public CategoryResponse updateCategory(Long categoryById, String name) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(categoryById)
                .orElseThrow(CategoryNotFoundException::new);
        
        category.setName(name);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public void deleteCategory(Long categoryById) throws CategoryNotFoundException {
        categoryRepository.findById(categoryById)
                .orElseThrow(CategoryNotFoundException::new);

        categoryRepository.deleteById(categoryById);
    }

    public Category createCategoryProduct(String name)   {
        Optional<Category> existingCategory = categoryRepository.findByName(name);

        if (existingCategory.isPresent()) {
            return existingCategory.get();
        }

        Category newCategory = Category.builder()
                .name(name)
                .build();

        return categoryRepository.save(newCategory);

    }

}
