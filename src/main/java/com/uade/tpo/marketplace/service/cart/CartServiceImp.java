package com.uade.tpo.marketplace.service.cart;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.controllers.cart.CartResponse;
import com.uade.tpo.marketplace.controllers.cart.ItemCartRequest;
import com.uade.tpo.marketplace.controllers.cart.ItemCartResponse;
import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.ItemCart;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.mapper.CartMapper;
import com.uade.tpo.marketplace.repository.CartRepository;
import com.uade.tpo.marketplace.repository.ItemCartRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemCartRepository itemCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartPriceCalculator cartPriceCalculator;

    @Autowired
    private CartMapper cartMapper;

    public CartResponse viewCart(long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        return cart.map(c -> cartMapper.toResponse(c, cartPriceCalculator)).orElseGet(CartResponse::new);
    }

    @Transactional
    public ItemCartResponse addToCart(ItemCartRequest itemCartRequest, User user) {
        if (itemCartRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Product product = productRepository.findById(itemCartRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        Optional <Cart> cart = cartRepository.findByUserId(user.getId());
        if (cart.isEmpty()) {
            cart = Optional.of(Cart.builder()
                    .user(user)
                    .items(new ArrayList<>()).dateTime(OffsetDateTime.now())
                    .build());
            cartRepository.save(cart.get());
        }
        Optional<ItemCart> itemCart = itemCartRepository.findByCartIdAndProductId(cart.get().getId(), product.getId());
        if (itemCart.isPresent()) {
            itemCart.get().setCount(itemCart.get().getCount() + itemCartRequest.getQuantity());
            itemCartRepository.save(itemCart.get());
        } else {
            itemCart = Optional.of(ItemCart.builder()
                    .cart(cart.get())
                    .product(product)
                    .count(itemCartRequest.getQuantity())
                    .build());
            itemCartRepository.save(itemCart.get());
        }
        touch(cart.get());
        return cartMapper.toItemResponse(itemCart.get());
    }

    @Transactional
    public CartResponse updateCart(ItemCartRequest itemCartRequest, long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        Optional<ItemCart> itemCart = itemCartRepository.findByCartIdAndProductId(cart.get().getId(), itemCartRequest.getProductId());
        if (cart.isEmpty()||itemCart.isEmpty()) {
            throw new IllegalArgumentException("Invalid cart or item");
        }

        if (itemCartRequest.getQuantity() < 0) {
            if (itemCart.get().getCount() + itemCartRequest.getQuantity() < 1) {
                throw new IllegalArgumentException("Invalid quantity");
            } else {
                itemCart.get().setCount(itemCart.get().getCount() + itemCartRequest.getQuantity());
                itemCartRepository.save(itemCart.get());
            }
        } else {
            itemCart.get().setCount(itemCartRequest.getQuantity() + itemCart.get().getCount());
            itemCartRepository.save(itemCart.get());
        }

        touch(cart.get());
        return cartMapper.toResponse(cart.get(), cartPriceCalculator);
    }


    @Transactional
    public void clearCart(long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cart not found for user");
        }
        itemCartRepository.deleteByCartId(cart.get().getId());
        touch(cart.get());
    }

    private void touch(Cart cart) {
        cart.setDateTime(OffsetDateTime.now());
        cartRepository.save(cart);
    }

}