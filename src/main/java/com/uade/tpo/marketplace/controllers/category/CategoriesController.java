package com.uade.tpo.marketplace.controllers.category;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.service.category.CategoryService;

@RestController
@RequestMapping("categories")
public class CategoriesController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Page<CategoryResponse> getAllCategories( @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
                if (page == null && size == null) {
                    return categoryService.getAllCategories(PageRequest.of(0, 10));
                }
        return categoryService.getAllCategories(PageRequest.of(page != null ? page : 0, size != null ? size : 10));
    }

    @GetMapping("{categoryById}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long categoryById) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryById));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest category) {
        CategoryResponse createdCategory = categoryService.createCategory(category.getName().toUpperCase());
        return ResponseEntity.created(URI.create("/categories/" + createdCategory.getId())).body(createdCategory);
    }

    @PutMapping("{categoryById}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long categoryById, @RequestBody CategoryRequest category)  {
        return ResponseEntity.ok(categoryService.updateCategory(categoryById, category.getName().toUpperCase()));
    }

    @DeleteMapping("{categoryById}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryById)  {
        categoryService.deleteCategory(categoryById);
        return ResponseEntity.noContent().build();
    }
}
