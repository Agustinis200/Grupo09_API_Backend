package com.uade.tpo.marketplace.mapper;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.controllers.product.ProductRequest;
import com.uade.tpo.marketplace.controllers.product.ProductResponse;
import com.uade.tpo.marketplace.entity.Product;

import java.sql.Blob;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public Product fromRequest(ProductRequest req) {
        if (req == null) return null;
        
        Product product = new Product();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setDiscount(req.getDiscount() != null ? req.getDiscount() : 1.0);
        product.setStock(req.getStock());
        return product;
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) return null;
        
        String imageFileBase64 = null;
        if (product.getImage() != null) {
            try {
                Blob blob = product.getImage().getImage();
                if (blob != null) {
                    byte[] bytes = blob.getBytes(1, (int) blob.length());
                    imageFileBase64 = Base64.getEncoder().encodeToString(bytes);
                }
            } catch (Exception e) {
                // En caso de error al leer la imagen, dejar como null
                imageFileBase64 = null;
            }
        }
        
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount() != null ? product.getDiscount() : 1.0)
                .stock(product.getStock())
                .category(product.getCategory() != null ? product.getCategory().getName() : null)
                .seller(product.getSeller() != null ? product.getSeller().getUsername() : null)
                .imageFile(imageFileBase64)
                .build();
    }

    public void updateEntityFromRequest(ProductRequest req, Product entity) {
        if (req == null || entity == null) return;
        
        if (req.getName() != null) entity.setName(req.getName());
        if (req.getDescription() != null) entity.setDescription(req.getDescription());
        if (req.getPrice() != null) entity.setPrice(req.getPrice());
        if (req.getDiscount() != null) entity.setDiscount(req.getDiscount());
        if (req.getStock() > 0) entity.setStock(req.getStock());
    }
    
    public List<ProductResponse> toResponseList(List<Product> products) {
        if (products == null) return List.of();
        return products.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
