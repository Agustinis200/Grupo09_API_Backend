package com.uade.tpo.marketplace.service.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.controllers.product.ProductRequest;
import com.uade.tpo.marketplace.controllers.product.ProductResponse;
import com.uade.tpo.marketplace.entity.User;

public interface ProductService {
    Page<ProductResponse> getAllProducts(Pageable pageable);
    ProductResponse getProductById(Long Productid);
    ProductResponse createProduct(ProductRequest productRequest, User user);
    ProductResponse updateProduct(ProductRequest productRequest, Long productId);
    void deleteProduct(Long Productid);
    void updateProductStock(Long productId,int quantity);
}
