package com.uade.tpo.marketplace.mapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.ItemCart;
import com.uade.tpo.marketplace.controllers.order.ItemOrderResponse;
import com.uade.tpo.marketplace.controllers.order.OrderRequest;
import com.uade.tpo.marketplace.controllers.order.OrderResponse;
import com.uade.tpo.marketplace.entity.ItemOrder;
import com.uade.tpo.marketplace.entity.Order;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.service.cart.CartPriceCalculator;

@Component
public class OrderMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Autowired
    private CartPriceCalculator cartPriceCalculator;

    public OrderMapper(CartPriceCalculator cartPriceCalculator) {
        this.cartPriceCalculator = cartPriceCalculator;
    }

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
        Order order = Order.builder()
                .dateTime(java.time.OffsetDateTime.now()) // Asegurar que la orden tenga fecha
                .address(orderRequest.getAddress())
                .status(orderRequest.getStatus() != null ? orderRequest.getStatus() : com.uade.tpo.marketplace.entity.enums.StateOrder.PENDING) // Valor por defecto
                .user(user)
                .totalPrice(cartPriceCalculator.total(cart))
                .  build();
        
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
        
        return ItemOrderResponse.builder()
                .id(item.getId())
                .productName(item.getProduct() != null ? item.getProduct().getName() : null)
                .count(item.getCount())
                .build();
    }

    public List<ItemOrderResponse> toItemResponseList(List<ItemOrder> items) {
        if (items == null) return new ArrayList<>();
        
        return items.stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());
    }


}
