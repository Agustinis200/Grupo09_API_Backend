package com.uade.tpo.marketplace.service.product;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.uade.tpo.marketplace.controllers.product.ProductRequest;
import com.uade.tpo.marketplace.controllers.product.ProductResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.mapper.ProductMapper;
import com.uade.tpo.marketplace.repository.ProductRepository;
import com.uade.tpo.marketplace.service.category.CategoryService;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        return productRepository.findById(productId).map(productMapper::toResponse).orElse(null);//Falta la exception
    }

    public ProductResponse createProduct(ProductRequest productRequest, User user) {
        Product product = productMapper.fromRequest(productRequest);
        product.setCategory(categoryService.createCategoryProduct(productRequest.getCategory()));
        product.setSeller(user);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    public ProductResponse updateProduct(ProductRequest productRequest, Long productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        productMapper.updateEntityFromRequest(productRequest, product.get());
        productRepository.save(product.get());
        return productMapper.toResponse(product.get());
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public void updateProductStock(Long productId, int quantity) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }

        
        if (product.get().getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product: " + productId);
        }
        product.get().setStock(product.get().getStock() - quantity);
        productRepository.save(product.get());
    }
}
