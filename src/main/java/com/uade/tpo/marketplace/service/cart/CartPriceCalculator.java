package com.uade.tpo.marketplace.service.cart;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.ItemCart;

@Component
public class CartPriceCalculator {
    public double total(Cart cart) {
        double total = 0;
        double discount = 0.1;
        for (ItemCart i : cart.getItems()) {
            double sub = i.getProduct().getPrice() * i.getCount();
            if (i.getCount() > 3) sub *= (1 - discount);
            total += sub;
        }
        return total;
    }
}