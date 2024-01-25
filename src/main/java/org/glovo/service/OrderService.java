package org.glovo.service;

import org.glovo.model.Order;
import org.glovo.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public Order getOrderById(String orderId) {
        return orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    public Order createOrder(Order order) {
        order.setId(generateOrderId());
        order.setCreation(new Date());
        orders.add(order);
        return order;
    }

    public Order updateOrder(String orderId, Order updatedOrder) {
        Order existingOrder = getOrderById(orderId);
        if (existingOrder != null) {
            existingOrder.setProducts(updatedOrder.getProducts());
            existingOrder.setModification(new Date());
        }
        return existingOrder;
    }

    public void deleteOrder(String orderId) {
        orders.removeIf(order -> order.getId().equals(orderId));
    }

    public Order addItemToOrder(String orderId, Product newItem) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.getProducts().add(newItem);
            updateOrder(orderId, order);
            return order;
        } else {
            return null;
        }
    }

    public Order removeItemFromOrder(String orderId, long itemId) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.getProducts().removeIf(product -> product.getId() == itemId);
            updateOrder(orderId, order);
            return order;
        } else {
            return null;
        }
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString();
    }
}