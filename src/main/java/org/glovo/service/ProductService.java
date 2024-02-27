package org.glovo.service;

import org.glovo.entity.Product;
import org.glovo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

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
