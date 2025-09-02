package com.uade.tpo.marketplace.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.ItemCart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.id = ?1")
    Optional<Cart> findByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cart c WHERE c.user.id = ?1")
    boolean existsByUserId(Long userId);

    @Query("SELECT i FROM Cart c JOIN c.items i WHERE i.product.id = ?1 AND c.user.id = ?2")
    Optional<ItemCart> findByProductId(Long productId, Long userId);

    @Query("select c from Cart c where c.dateTime < :limit")
    List<Cart> findOlderThan(@Param("limit") OffsetDateTime limit);

    @Query("SELECT c FROM Cart c WHERE c.dateTime < ?1")
    List<Cart> findByDateTimeBefore(OffsetDateTime limit);
}
