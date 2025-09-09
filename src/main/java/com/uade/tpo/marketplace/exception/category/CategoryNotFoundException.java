package com.uade.tpo.marketplace.exception.category;


public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super("Category not found");
    }

}
