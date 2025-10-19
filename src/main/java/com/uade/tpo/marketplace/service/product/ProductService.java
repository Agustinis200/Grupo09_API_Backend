package com.uade.tpo.marketplace.service.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.controllers.product.ProductRequest;
import com.uade.tpo.marketplace.controllers.product.ProductResponse;
import com.uade.tpo.marketplace.entity.User;

public interface ProductService {
    Page<ProductResponse> getAllProducts(Pageable pageable);
    Page<ProductResponse> searchProducts(String query , Pageable pageable);
    Page<ProductResponse> getProductsByCategory(String categoryName, Pageable pageable, String sortByPrice, Boolean onSale);
    Page<ProductResponse> getProductsOnSale(Pageable pageable, String sortByPrice);
    Page<ProductResponse> getProductsOutOfStock(Pageable pageable);
    ProductResponse getProductById(Long Productid);
    ProductResponse createProduct(ProductRequest productRequest, User user);
    ProductResponse createProductWithImage(ProductRequest productRequest, MultipartFile image, User user);
    ProductResponse updateProduct(ProductRequest productRequest, Long productId);
    void deleteProduct(Long Productid);
    void updateProductStock(Long productId,int quantity);
}
