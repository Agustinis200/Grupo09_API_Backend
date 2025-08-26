package com.uade.tpo.marketplace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.uade.tpo.marketplace.entity.Category;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.repository.ProductRepository;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(String name, String description, Double price, String imageUrl, String categoryName) {
        Category categoryObj = productRepository.findCategory(categoryName);
        if (categoryObj == null) {
            throw new IllegalArgumentException("Category not found: " + categoryName);
        }
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .imageUrl(imageUrl)
                .category(categoryObj)
                .stock(0) // Inicializar stock en 0
                .build();
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, String name, String description, Double price, String imageUrl) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            return productRepository.save(product);
        }
        return null;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


}
