package com.uade.tpo.marketplace.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.ItemCart;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.repository.CartRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;
import com.uade.tpo.marketplace.repository.UserRepository;

@Service
public class CartServiceImp implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cart cart = Cart.builder()
                .user(user)
                .build();

        return cartRepository.save(cart);
    }

    @Override
    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }

    @Override
    public Cart addItem(Long cartId, Long productId, int count) throws Exception {
        Cart cart = getCart(cartId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Producto no encontrado"));

        if (product.getStock() < count) {
            throw new Exception("Stock insuficiente");
        }

        // Si ya existe el producto en el carrito → actualizar cantidad
        Optional<ItemCart> existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            ItemCart item = existingItem.get();
            item.setCount(item.getCount() + count);
        } else {
            ItemCart newItem = ItemCart.builder()
                    .cart(cart)
                    .product(product)
                    .count(count)
                    .build();
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItem(Long cartId, Long itemId) throws Exception {
        Cart cart = getCart(cartId);
        boolean removed = cart.getItems().removeIf(i -> i.getId().equals(itemId));
        if (!removed) throw new Exception("Item no encontrado en el carrito");
        return cartRepository.save(cart);
    }

    @Override
    public Cart checkout(Long cartId) throws Exception {
        Cart cart = getCart(cartId);

        for (ItemCart item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getCount()) {
                throw new Exception("Stock insuficiente para " + product.getName());
            }
            product.setStock(product.getStock() - item.getCount());
            productRepository.save(product);
        }

        // vaciar carrito
        cart.getItems().clear();
        return cartRepository.save(cart);
    }
}