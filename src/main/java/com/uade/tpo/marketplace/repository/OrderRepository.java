package com.uade.tpo.marketplace.repository;
import com.uade.tpo.marketplace.entity.Order;
import com.uade.tpo.marketplace.entity.enums.StateOrder;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.user.id = ?1")
    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.status = ?1")
    Optional<Order> findByStatus(StateOrder status);

    @Query("SELECT o FROM Order o WHERE o.id = ?1 AND o.user.id = ?2")
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
