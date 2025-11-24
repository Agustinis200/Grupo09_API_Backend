package com.uade.tpo.marketplace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uade.tpo.marketplace.entity.ItemCart;
import com.uade.tpo.marketplace.entity.Product;


public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {
    @Query("SELECT ic.product FROM ItemCart ic WHERE ic.id = :id")
    Product findItemById(Long id);

    @Query("SELECT ic FROM ItemCart ic WHERE ic.cart.id = :cartId AND ic.product.id = :productId")
    Optional<ItemCart> findByCartIdAndProductId(Long cartId, Long productId);

    @Modifying
    @Query("delete from ItemCart ic where ic.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);

    @Modifying
    @Query("delete from ItemCart ic where ic.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}
