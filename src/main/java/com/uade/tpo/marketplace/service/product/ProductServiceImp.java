package com.uade.tpo.marketplace.service.product;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.controllers.product.ProductRequest;
import com.uade.tpo.marketplace.controllers.product.ProductResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.mapper.ProductMapper;
import com.uade.tpo.marketplace.repository.ProductRepository;
import com.uade.tpo.marketplace.service.category.CategoryService;
import com.uade.tpo.marketplace.exception.product.*;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductMapper productMapper;

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest, User user) {
        Product product = productMapper.fromRequest(productRequest);
        product.setCategory(categoryService.createCategoryProduct(productRequest.getCategory()));
        product.setSeller(user);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(ProductRequest productRequest, Long productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found: " + productId);
        }
        productMapper.updateEntityFromRequest(productRequest, product.get());
        productRepository.save(product.get());
        return productMapper.toResponse(product.get());
    }

    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found: " + productId);
        }
        productRepository.deleteById(productId);
    }

    @Transactional
    public void updateProductStock(Long productId, int quantity) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found: " + productId);
        }
        
        if (product.get().getStock() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + productId);
        }
        product.get().setStock(product.get().getStock() - quantity);
        productRepository.save(product.get());
    }
}
