package org.glovo.service;

import lombok.AllArgsConstructor;
import org.glovo.model.GlovoOrder;
import org.glovo.model.Product;
import org.glovo.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private List<Product> products;

    public List<GlovoOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public GlovoOrder getById(long orderId) {
        return orderRepository.getReferenceById(orderId);
    }

    public GlovoOrder create(GlovoOrder order) {
        order.setCreation(new Date());
        order.setTotalPrice(calculateTotalPrice());
        return orderRepository.save(order);
    }

    public GlovoOrder update(long orderId, GlovoOrder updatedOrder) {
        GlovoOrder existingOrder = getById(orderId);
        if (existingOrder != null) {
            existingOrder.setProducts(updatedOrder.getProducts());
            existingOrder.setModification(new Date());
            orderRepository.save(existingOrder);
        }
        return existingOrder;
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
        order.getProducts().removeIf(p -> p.getId() == itemId);
        return orderRepository.save(order);
    }

    public double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (Product product : products) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }
}