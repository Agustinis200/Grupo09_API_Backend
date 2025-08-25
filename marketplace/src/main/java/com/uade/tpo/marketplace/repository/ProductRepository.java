package com.uade.tpo.marketplace.repository;

import java.util.List;
import com.uade.tpo.marketplace.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.category.name = ?1")
    List<Product> findByCategoryName(String categoryName);

    @Query("SELECT c FROM Category c WHERE c.name = ?1")
    Category findCategory(String category);
}
