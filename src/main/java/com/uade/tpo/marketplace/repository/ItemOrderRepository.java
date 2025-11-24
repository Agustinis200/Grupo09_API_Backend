package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uade.tpo.marketplace.entity.ItemOrder;

public interface ItemOrderRepository extends JpaRepository<ItemOrder, Long> {

    @Query("SELECT COUNT(io) > 0 FROM ItemOrder io WHERE io.product.id = :productId")
    boolean existsByProductId(@Param("productId") Long productId);

}
