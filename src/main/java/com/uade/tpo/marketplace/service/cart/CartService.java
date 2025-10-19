package com.uade.tpo.marketplace.service.cart;

import com.uade.tpo.marketplace.controllers.cart.CartResponse;
import com.uade.tpo.marketplace.controllers.cart.ItemCartRequest;
import com.uade.tpo.marketplace.controllers.cart.ItemCartResponse;
import com.uade.tpo.marketplace.entity.User;

public interface CartService {
    public CartResponse viewCart(long userId);

    public ItemCartResponse addToCart(ItemCartRequest itemCartRequest, User user);

    public CartResponse updateCart(ItemCartRequest itemCartRequest, long userId);

    public void clearCart(long userId);

    public void removeItemFromCart(long userId, long productId);
}
