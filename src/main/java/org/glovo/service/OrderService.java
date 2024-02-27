package org.glovo.service;

import org.glovo.entity.GlovoOrder;
import org.glovo.entity.Product;
import org.glovo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    public List<GlovoOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public GlovoOrder getById(long orderId) {
        return orderRepository.getReferenceById(orderId);
    }

    public GlovoOrder create(GlovoOrder order) {
        order.setCreation(new Date());
        order.setTotalPrice(calculateTotalPrice(order));
        order.setProductQuantity(order.getProducts().size());
        return orderRepository.save(order);
    }

    public GlovoOrder update(long orderId, GlovoOrder updatedOrder) {
        GlovoOrder existingOrder = getById(orderId);
        if (existingOrder != null) {
            existingOrder.setProducts(updatedOrder.getProducts());
            existingOrder.setModification(new Date());
            return orderRepository.save(existingOrder);
        }
        return null;
    }

    public void delete(long orderId) {
        orderRepository.deleteById(orderId);
    }

    public GlovoOrder addProduct(long orderId, long itemId) {
        GlovoOrder order = orderRepository.getReferenceById(orderId);
        Product newProduct = productService.getProduct(itemId);
        order.getProducts().add(newProduct);
        return orderRepository.save(order);
    }

    public GlovoOrder removeProduct(long orderId, long itemId) {
        GlovoOrder order = orderRepository.getReferenceById(orderId);
        if (order.getProducts() == null) {
            order.setProducts(new ArrayList<>());
        }
        order.getProducts().removeIf(p -> p.getId() == itemId);
        return orderRepository.save(order);
    }

    public double calculateTotalPrice(GlovoOrder order) {
        double totalPrice = 0.0;
        for (Product product : order.getProducts()) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }
}