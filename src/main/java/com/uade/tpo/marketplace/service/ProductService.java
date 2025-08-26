package com.uade.tpo.marketplace.service;

import java.util.List;

import com.uade.tpo.marketplace.entity.Product;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product createProduct(String name, String description, Double price, String imageUrl, String category);
    Product updateProduct(Long id, String name, String description, Double price, String imageUrl);
    void deleteProduct(Long id);
}
