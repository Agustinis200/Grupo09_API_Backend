package com.uade.tpo.marketplace.service.product;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.controllers.product.ProductRequest;
import com.uade.tpo.marketplace.controllers.product.ProductResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.Image;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.mapper.ProductMapper;
import com.uade.tpo.marketplace.repository.ImageRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;
import com.uade.tpo.marketplace.service.category.CategoryService;
import com.uade.tpo.marketplace.exception.product.*;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private com.uade.tpo.marketplace.repository.ItemCartRepository itemCartRepository;
    
    @Autowired
    private com.uade.tpo.marketplace.repository.ItemOrderRepository itemOrderRepository;
    
    @Autowired
    private ProductMapper productMapper;

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAllOrderByIdDesc(pageable).map(productMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(String categoryName, Pageable pageable, String sortByPrice, Boolean onSale) {
        // Si onSale es true, filtrar solo productos con descuento
        if (Boolean.TRUE.equals(onSale)) {
            if ("asc".equalsIgnoreCase(sortByPrice)) {
                return productRepository.findByCategoryNameOnSaleByPriceAsc(categoryName, pageable).map(productMapper::toResponse);
            } else if ("desc".equalsIgnoreCase(sortByPrice)) {
                return productRepository.findByCategoryNameOnSaleByPriceDesc(categoryName, pageable).map(productMapper::toResponse);
            } else {
                return productRepository.findByCategoryNameOnSale(categoryName, pageable).map(productMapper::toResponse);
            }
        }
        
        // Si onSale es false o null, mostrar todos los productos de la categoría
        if ("asc".equalsIgnoreCase(sortByPrice)) {
            return productRepository.findByCategoryNamePagedByPriceAsc(categoryName, pageable).map(productMapper::toResponse);
        } else if ("desc".equalsIgnoreCase(sortByPrice)) {
            return productRepository.findByCategoryNamePagedByPriceDesc(categoryName, pageable).map(productMapper::toResponse);
        } else {
            return productRepository.findByCategoryNamePaged(categoryName, pageable).map(productMapper::toResponse);
        }
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsOnSale(Pageable pageable, String sortByPrice) {
        if ("asc".equalsIgnoreCase(sortByPrice)) {
            return productRepository.findProductsOnSaleByPriceAsc(pageable).map(productMapper::toResponse);
        } else if ("desc".equalsIgnoreCase(sortByPrice)) {
            return productRepository.findProductsOnSaleByPriceDesc(pageable).map(productMapper::toResponse);
        } else {
            return productRepository.findProductsOnSale(pageable).map(productMapper::toResponse);
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String query, Pageable pageable) {
        return productRepository.searchProducts(query, pageable).map(productMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsOutOfStock(Pageable pageable) {
        return productRepository.findProductsOutOfStock(pageable).map(productMapper::toResponse);
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
    public ProductResponse createProductWithImage(ProductRequest productRequest, MultipartFile imageFile, User user) {
        try {
            // 1. Crear el producto
            Product product = productMapper.fromRequest(productRequest);
            product.setCategory(categoryService.createCategoryProduct(productRequest.getCategory()));
            product.setSeller(user);
            product = productRepository.save(product);
            
            // 2. Si hay imagen, guardarla y asociarla al producto
            if (imageFile != null && !imageFile.isEmpty()) {
                byte[] bytes = imageFile.getBytes();
                Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

                Image image = Image.builder()
                        .image(blob)
                        .product(product)
                        .build();

                image = imageRepository.save(image);

                // Asociar la imagen guardada al producto y guardar el producto
                product.setImage(image);
                productRepository.save(product);
            }
            
            // 3. Retornar la respuesta del producto
            return productMapper.toResponse(product);
            
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de imagen: " + e.getMessage(), e);
        } catch (SerialException e) {
            throw new RuntimeException("Error al serializar la imagen: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la imagen en la base de datos: " + e.getMessage(), e);
        }
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
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found: " + productId);
        }
        
        // Verificar si el producto está en alguna orden
        if (itemOrderRepository.existsByProductId(productId)) {
            throw new RuntimeException("No se puede eliminar el producto porque está presente en órdenes existentes");
        }
        
        // Eliminar items del carrito que referencian este producto
        itemCartRepository.deleteByProductId(productId);
        
        // Eliminar imagen si existe
        if (product.get().getImage() != null) {
            imageRepository.delete(product.get().getImage());
        }
        
        // Finalmente eliminar el producto
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
