package com.uade.tpo.marketplace.service;

import java.util.List;

import com.uade.tpo.marketplace.entity.Product;

public interface ProductService {

    public List<Product> getAllProducts();

    public Product getProductById(Long id);

    public Product createProduct(String name, String description, Double price, String imageUrl, String category);

    public Product updateProduct(Long id, String name, String description, Double price, String imageUrl);

    public void deleteProduct(Long id);
}
