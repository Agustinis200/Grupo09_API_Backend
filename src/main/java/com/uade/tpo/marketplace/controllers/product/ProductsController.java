package com.uade.tpo.marketplace.controllers.product;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.service.product.ProductService;

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
    public ResponseEntity<Page<ProductResponse>> getAllProducts(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size) {
        if (page == null && size == null) {
            return ResponseEntity.ok(productService.getAllProducts(PageRequest.of(0, 10)));   
        }
        return ResponseEntity.ok(productService.getAllProducts(PageRequest.of(page, size)));
    }

    @GetMapping("{Productid}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long Productid) {
        return ResponseEntity.ok(productService.getProductById(Productid));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest,@AuthenticationPrincipal User user) {
        ProductResponse createdProduct = productService.createProduct(productRequest, user);
        return ResponseEntity.created(URI.create("/products/" + createdProduct.getId())).body(createdProduct);
    }

    @PutMapping("{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest,@PathVariable Long productId) {
        ProductResponse updatedProduct = productService.updateProduct(productRequest, productId);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }

}
