package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.ItemOrder;

public interface ItemOrderRepository extends JpaRepository<ItemOrder, Long> {

}
