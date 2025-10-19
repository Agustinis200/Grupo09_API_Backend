package com.uade.tpo.marketplace.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.category.name = ?1")
    List<Product> findByCategoryName(String categoryName);

    @Query("SELECT p FROM Product p WHERE p.category.name = ?1 ORDER BY p.id DESC")
    Page<Product> findByCategoryNamePaged(String categoryName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = ?1 ORDER BY (p.price * p.discount) ASC")
    Page<Product> findByCategoryNamePagedByPriceAsc(String categoryName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = ?1 ORDER BY (p.price * p.discount) DESC")
    Page<Product> findByCategoryNamePagedByPriceDesc(String categoryName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = ?1 AND p.discount < 1.0 ORDER BY p.id DESC")
    Page<Product> findByCategoryNameOnSale(String categoryName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = ?1 AND p.discount < 1.0 ORDER BY (p.price * p.discount) ASC")
    Page<Product> findByCategoryNameOnSaleByPriceAsc(String categoryName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.name = ?1 AND p.discount < 1.0 ORDER BY (p.price * p.discount) DESC")
    Page<Product> findByCategoryNameOnSaleByPriceDesc(String categoryName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.discount < 1.0 ORDER BY p.id DESC")
    Page<Product> findProductsOnSale(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.discount < 1.0 ORDER BY (p.price * p.discount) ASC")
    Page<Product> findProductsOnSaleByPriceAsc(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.discount < 1.0 ORDER BY (p.price * p.discount) DESC")
    Page<Product> findProductsOnSaleByPriceDesc(Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.id DESC")
    Page<Product> findAllOrderByIdDesc(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Product> searchProducts(String query, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stock = 0 ORDER BY p.id DESC")
    Page<Product> findProductsOutOfStock(Pageable pageable);

}
