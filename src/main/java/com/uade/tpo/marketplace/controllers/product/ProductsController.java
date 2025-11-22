package com.uade.tpo.marketplace.controllers.product;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.service.product.ProductService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("products")
public class ProductsController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size ) {
        if (page == null && size == null) {
            return ResponseEntity.ok(productService.getAllProducts(PageRequest.of(0, 10)));   
        }
        return ResponseEntity.ok(productService.getAllProducts(PageRequest.of(page, size)));
    }
    
    @GetMapping("/sale")
    public ResponseEntity<Page<ProductResponse>> getProductsOnSale(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortByPrice) {
        if (page == null && size == null) {
            return ResponseEntity.ok(productService.getProductsOnSale(PageRequest.of(0, 10), sortByPrice));   
        }
        return ResponseEntity.ok(productService.getProductsOnSale(PageRequest.of(page, size), sortByPrice));
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
            @PathVariable String categoryName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortByPrice,
            @RequestParam(required = false) Boolean onSale) {
        if (page == null && size == null) {
            return ResponseEntity.ok(productService.getProductsByCategory(categoryName, PageRequest.of(0, 10), sortByPrice, onSale));   
        }
        return ResponseEntity.ok(productService.getProductsByCategory(categoryName, PageRequest.of(page, size), sortByPrice, onSale));
    }
    

    @GetMapping("{productid}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productid) {
        return ResponseEntity.ok(productService.getProductById(productid));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam String query,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null && size == null) {
            return ResponseEntity.ok(productService.searchProducts(query, PageRequest.of(0, 10)));
        }
        return ResponseEntity.ok(productService.searchProducts(query, PageRequest.of(page, size)));
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<Page<ProductResponse>> getProductsOutOfStock(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null && size == null) {
            return ResponseEntity.ok(productService.getProductsOutOfStock(PageRequest.of(0, 10)));
        }
        return ResponseEntity.ok(productService.getProductsOutOfStock(PageRequest.of(page, size)));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest productRequest,
            @AuthenticationPrincipal User user) {
        
        ProductResponse createdProduct = productService.createProduct(productRequest, user);
        return ResponseEntity.created(URI.create("/products/" + createdProduct.getId())).body(createdProduct);
    }

    @PostMapping(value = "/with-image", consumes = "multipart/form-data")
    public ResponseEntity<ProductResponse> createProductWithImage(
            @RequestPart("product") ProductRequest productRequest,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal User user) {
        
        ProductResponse createdProduct = productService.createProductWithImage(productRequest, image, user);
        return ResponseEntity.created(URI.create("/products/" + createdProduct.getId())).body(createdProduct);
    }

    @PutMapping("{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest,@PathVariable Long productId) {
        ProductResponse updatedProduct = productService.updateProduct(productRequest, productId);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}
