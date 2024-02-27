package org.glovo.controller;

import lombok.AllArgsConstructor;
import org.glovo.entity.GlovoOrder;
import org.glovo.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping()
    public List<GlovoOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public GlovoOrder getOrderById(@PathVariable long orderId) {
        return orderService.getById(orderId);
    }

    @PutMapping()
    public GlovoOrder updateOrder(@RequestBody GlovoOrder update) {
        return orderService.update(update.getId(), update);
    }

    @PostMapping()
    public GlovoOrder saveOrder(@RequestBody GlovoOrder order) {
        return orderService.create(order);
    }

    @PatchMapping("/{orderId}/add-item/{itemId}")
    public GlovoOrder addItemToOrder(@PathVariable long orderId, @PathVariable long itemId) {
        return orderService.addProduct(orderId, itemId);
    }

    @DeleteMapping("/{orderId}/remove-item/{itemId}")
    public GlovoOrder removeItemFromOrder(@PathVariable long orderId, @PathVariable long itemId) {
        return orderService.removeProduct(orderId, itemId);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable long orderId) {
        orderService.delete(orderId);
    }
}
