package com.uade.tpo.marketplace.mapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.ItemCart;
import com.uade.tpo.marketplace.controllers.order.ItemOrderResponse;
import com.uade.tpo.marketplace.controllers.order.OrderRequest;
import com.uade.tpo.marketplace.controllers.order.OrderResponse;
import com.uade.tpo.marketplace.entity.ItemOrder;
import com.uade.tpo.marketplace.entity.Order;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.User;

@Component
public class OrderMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Order toEntity(OrderRequest req) {
        if (req == null) return null;

        Order order = Order.builder()
                .dateTime(java.time.OffsetDateTime.now())
                .address(req.getAddress())
                .status(req.getStatus())
                .build();
        return order;
    }
    
    public Order fromCart(Cart cart, User user, OrderRequest orderRequest) {        
        // Calcular el precio total del carrito usando los descuentos de cada producto
        double totalPrice = calculateCartTotalPrice(cart);
        
        Order order = Order.builder()
                .dateTime(java.time.OffsetDateTime.now()) // Asegurar que la orden tenga fecha
                .address(orderRequest.getAddress())
                .status(orderRequest.getStatus() != null ? orderRequest.getStatus() : com.uade.tpo.marketplace.entity.enums.StateOrder.PENDING) // Valor por defecto
                .user(user)
                .totalPrice(totalPrice)
                .build();
        
        List<ItemOrder> itemOrders = new ArrayList<>();
        if (cart.getItems() != null) {
            for (ItemCart itemCart : cart.getItems()) {
                ItemOrder itemOrder = ItemOrder.builder()
                        .count(itemCart.getCount())
                        .product(itemCart.getProduct())
                        .order(order)
                        .build();
                itemOrders.add(itemOrder);
            }
        }
        
        order.setItems(itemOrders);
        
        return order;
    }

    public OrderResponse toResponse(Order order) {
        if (order == null) return null;
        
        return OrderResponse.builder()
                .id(order.getId())
                .dateTime(order.getDateTime() != null ? order.getDateTime().format(DATE_FORMATTER) : null)
                .address(order.getAddress())
                .items(toItemResponseList(order.getItems()))
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .totalPrice(order.getTotalPrice())
                .build();
    }

    public ItemOrderResponse toItemResponse(ItemOrder item) {
        if (item == null) return null;
        
        Product product = item.getProduct();
        if (product == null) return null;
        
        double price = product.getPrice() != null ? product.getPrice() : 0.0;
        double discount = product.getDiscount() != null ? product.getDiscount() : 1.0;
        int count = item.getCount();
        double subtotal = price * discount * count;
        
        return ItemOrderResponse.builder()
                .id(item.getId())
                .productName(product.getName())
                .productPrice(price)
                .productDiscount(discount)
                .count(count)
                .subtotal(subtotal)
                .build();
    }

    public List<ItemOrderResponse> toItemResponseList(List<ItemOrder> items) {
        if (items == null) return new ArrayList<>();
        
        return items.stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el precio total del carrito aplicando el descuento de cada producto
     * @param cart El carrito de compras
     * @return El precio total con descuentos aplicados
     */
    private double calculateCartTotalPrice(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return 0.0;
        }
        
        return cart.getItems().stream()
            .mapToDouble(item -> {
                Product product = item.getProduct();
                if (product == null) return 0.0;
                
                double price = product.getPrice() != null ? product.getPrice() : 0.0;
                double discount = product.getDiscount() != null ? product.getDiscount() : 1.0;
                int quantity = item.getCount();
                
                // Precio final = precio * multiplicador de descuento * cantidad
                return price * discount * quantity;
            })
            .sum();
    }

}
