package com.uade.tpo.marketplace.controllers.category;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exception.CategoryDuplicateException;
import com.uade.tpo.marketplace.exception.CategoryNotFoundException;
import com.uade.tpo.marketplace.service.CategoryService;

@RestController
@RequestMapping("categories")
public class CategoriesController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("{categoryById}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryById) throws CategoryNotFoundException{
        return ResponseEntity.ok(categoryService.getCategoryById(categoryById));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest category) throws CategoryDuplicateException {
        Category createdCategory = categoryService.createCategory(category.getName());
        return ResponseEntity.created(URI.create("/categories/" + createdCategory.getId())).body(createdCategory);
    }

    @PutMapping("{categoryById}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryById, @RequestBody CategoryRequest category) throws CategoryNotFoundException {
        return ResponseEntity.ok(categoryService.updateCategory(categoryById, category.getName()));
    }

    @DeleteMapping("{categoryById}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryById) throws CategoryNotFoundException {
        categoryService.deleteCategory(categoryById);
        return ResponseEntity.noContent().build();
    }
}
