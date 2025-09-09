package com.uade.tpo.marketplace.service.cart;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.uade.tpo.marketplace.exception.cart.*;
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

    @Transactional(readOnly = true)
    public CartResponse viewCart(long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        return cart.map(c -> cartMapper.toResponse(c, cartPriceCalculator)).orElseGet(CartResponse::new);
    }

    @Transactional
    public ItemCartResponse addToCart(ItemCartRequest itemCartRequest, User user) {
        // Validar la cantidad
        if (itemCartRequest.getQuantity() <= 0) {
            throw new InvalidQuantityException("La cantidad debe ser mayor que cero");
        }

        // Buscar el producto
        Product product = productRepository.findById(itemCartRequest.getProductId())
                .orElseThrow(() -> new ItemNotFoundInCartException("Producto no encontrado con ID: " + itemCartRequest.getProductId()));

        // Buscar o crear el carrito
        Optional<Cart> cartOpt = cartRepository.findByUserId(user.getId());
        Cart cart;
        
        if (cartOpt.isEmpty()) {
            // Crear un nuevo carrito para el usuario
            cart = Cart.builder()
                    .user(user)
                    .items(new ArrayList<>())
                    .dateTime(OffsetDateTime.now())
                    .build();
            cart = cartRepository.save(cart);
        } else {
            cart = cartOpt.get();
        }
        
        // Buscar si el producto ya está en el carrito
        Optional<ItemCart> itemCartOpt = itemCartRepository.findByCartIdAndProductId(cart.getId(), product.getId());
        ItemCart itemCart;
        
        if (itemCartOpt.isPresent()) {
            // Actualizar la cantidad si el producto ya está en el carrito
            itemCart = itemCartOpt.get();
            itemCart.setCount(itemCart.getCount() + itemCartRequest.getQuantity());
        } else {
            // Añadir un nuevo item al carrito
            itemCart = ItemCart.builder()
                    .cart(cart)
                    .product(product)
                    .count(itemCartRequest.getQuantity())
                    .build();
        }
        
        // Guardar el item del carrito
        itemCart = itemCartRepository.save(itemCart);
        
        // Actualizar la fecha del carrito
        touch(cart);
        
        return cartMapper.toItemResponse(itemCart);
    }

    @Transactional
    public CartResponse updateCart(ItemCartRequest itemCartRequest, long userId) {
        // Buscar el carrito del usuario
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isEmpty()) {
            throw new CartNotFoundException("No se encontró el carrito para el usuario con ID: " + userId);
        }
        
        Cart cart = cartOpt.get();
        
        // Buscar el item en el carrito
        Optional<ItemCart> itemCartOpt = itemCartRepository.findByCartIdAndProductId(cart.getId(), itemCartRequest.getProductId());
        if (itemCartOpt.isEmpty()) {
            throw new ItemNotFoundInCartException("Producto con ID: " + itemCartRequest.getProductId() + " no encontrado en el carrito");
        }
        
        ItemCart itemCart = itemCartOpt.get();
        
        // Actualizar la cantidad según el valor recibido
        if (itemCartRequest.getQuantity() < 0) {
            // Si es negativo, se está intentando reducir la cantidad
            int newCount = itemCart.getCount() + itemCartRequest.getQuantity();
            if (newCount < 1) {
                throw new InvalidQuantityException("La cantidad resultante debe ser al menos 1. Actual: " + 
                    itemCart.getCount() + ", Ajuste: " + itemCartRequest.getQuantity());
            }
            itemCart.setCount(newCount);
        } else {
            // Si es positivo o cero, se está intentando establecer o aumentar la cantidad
            itemCart.setCount(itemCart.getCount() + itemCartRequest.getQuantity());
        }
        
        // Guardar el item actualizado
        itemCartRepository.save(itemCart);
        
        // Actualizar la fecha del carrito
        touch(cart);
        
        return cartMapper.toResponse(cart, cartPriceCalculator);
    }


    @Transactional
    public void clearCart(long userId) {
        // Buscar el carrito del usuario
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isEmpty()) {
            throw new CartNotFoundException("No se encontró el carrito para el usuario con ID: " + userId);
        }
        
        // Eliminar todos los items del carrito
        itemCartRepository.deleteByCartId(cartOpt.get().getId());
        
        // Actualizar la fecha del carrito
        touch(cartOpt.get());
    }

    private void touch(Cart cart) {
        cart.setDateTime(OffsetDateTime.now());
        cartRepository.save(cart);
    }

}