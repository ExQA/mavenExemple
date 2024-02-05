package org.glovo.service;

import lombok.AllArgsConstructor;
import org.glovo.model.Product;
import org.glovo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProduct(long productId) {
        return repository.getReferenceById(productId);
    }

    public Product create(Product product) {
        return repository.save(product);
    }

    public void delete(long productId) {
        repository.deleteById(productId);
    }
}
