package com.uade.tpo.marketplace.config;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.repository.CartRepository;
import com.uade.tpo.marketplace.repository.ItemCartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.uade.tpo.marketplace.entity.Cart;

@Component
@RequiredArgsConstructor
public class CartCleanupOnStartup implements CommandLineRunner {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemCartRepository itemCartRepository;

    @Override
    @Transactional
    public void run(String... args) {
        OffsetDateTime limit = OffsetDateTime.now().minusHours(24);
        // Trae todos los carts cuya última modificación es anterior a 24h
        List<Cart> oldCarts = cartRepository.findByDateTimeBefore(limit);

        for (Cart c : oldCarts) {
            itemCartRepository.deleteByCartId(c.getId());   // borra todos los items del cart
            c.setDateTime(OffsetDateTime.now());  // registra la limpieza
        }
        cartRepository.saveAll(oldCarts);
    }
}