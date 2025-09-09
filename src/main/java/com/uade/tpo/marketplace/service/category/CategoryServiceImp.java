package com.uade.tpo.marketplace.service.category;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.controllers.category.CategoryResponse;
import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exception.category.CategoryDuplicateException;
import com.uade.tpo.marketplace.exception.category.CategoryNotFoundException;
import com.uade.tpo.marketplace.mapper.CategoryMapper;
import com.uade.tpo.marketplace.repository.CategoryRepository;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long categoryById){
        return categoryRepository.findById(categoryById)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException());
    }

    @Transactional
    public CategoryResponse createCategory(String name) {
        Optional<Category> existingCategory = categoryRepository.findByName(name);

        if (existingCategory.isPresent()) {
            throw new CategoryDuplicateException("La categoría " + name + " ya existe");
        }
        
        Category newCategory = Category.builder()
                .name(name)
                .build();
        
        Category savedCategory = categoryRepository.save(newCategory);
        
        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse updateCategory(Long categoryById, String name){
        Category category = categoryRepository.findById(categoryById)
                .orElseThrow(() -> new CategoryNotFoundException());
        
        // Verificar si ya existe una categoría con el nuevo nombre
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(categoryById)) {
            throw new CategoryDuplicateException("La categoría " + name + " ya existe");
        }
        
        category.setName(name);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long categoryById){
        // Verificar que la categoría exista antes de eliminarla
        if (!categoryRepository.existsById(categoryById)) {
            throw new CategoryNotFoundException();
        }

        categoryRepository.deleteById(categoryById);
    }

    @Transactional
    public Category createCategoryProduct(String name) {
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
