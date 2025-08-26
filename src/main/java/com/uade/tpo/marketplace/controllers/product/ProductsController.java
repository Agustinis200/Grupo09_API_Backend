package com.uade.tpo.marketplace.controllers.product;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.service.ProductService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("products")
public class ProductsController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("{Productid}")
    public ResponseEntity<Product> getProductById(@PathVariable Long Productid) {
        Product product = productService.getProductById(Productid);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
        Product createdProduct = productService.createProduct(productRequest.getName(), productRequest.getDescription(), productRequest.getPrice(), productRequest.getImageUrl(), productRequest.getCategory().getName());
        return ResponseEntity.created(URI.create("/products/" + createdProduct.getId())).body(createdProduct);
    }

    @PutMapping("{Productid}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long Productid, @RequestBody ProductRequest productRequest) {
        Product updatedProduct = productService.updateProduct(Productid, productRequest.getName(), productRequest.getDescription(), productRequest.getPrice(), productRequest.getImageUrl());
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("{Productid}")
    public void deleteProduct(@PathVariable Long Productid) {
        productService.deleteProduct(Productid);
    }

}
