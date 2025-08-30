package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.entity.Cart;

public interface CartService {

    public Cart createCart(Long userId);

    public Cart getCart(Long cartId);

    public Cart addItem(Long cartId, Long productId, int count) throws Exception;

    public Cart removeItem(Long cartId, Long itemId) throws Exception;

    public Cart checkout(Long cartId) throws Exception;
}