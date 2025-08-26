package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.uade.tpo.marketplace.entity.Cart;

@Repository
public interface CartRepository {
    @Query("SELECT c FROM Cart c JOIN c.user u")
    List<Cart> findAllCarts();
}
