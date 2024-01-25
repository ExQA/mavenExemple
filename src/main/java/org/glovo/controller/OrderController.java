package org.glovo.controller;

import lombok.AllArgsConstructor;
import org.glovo.model.Order;
import org.glovo.model.Product;
import org.glovo.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping()
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping()
    public Order updateOrder(@RequestBody Order update) {
        return orderService.updateOrder(update.getId(), update);
    }

    @PostMapping()
    public Order saveOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @PatchMapping("/{orderId}/add-item")
    public Order addItemToOrder(@PathVariable String orderId, @RequestBody Product newItem) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            order.getProducts().add(newItem);
            return orderService.updateOrder(orderId, order);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{orderId}/remove-item/{itemId}")
    public Order removeItemFromOrder(@PathVariable String orderId, @PathVariable long itemId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            order.getProducts().removeIf(product -> product.getId() == itemId);
            return orderService.updateOrder(orderId, order);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
    }
}