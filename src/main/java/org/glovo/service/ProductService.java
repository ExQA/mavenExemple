package org.glovo.service;

import org.glovo.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>();

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Product getProductById(long productId) {
        return products.stream()
                .filter(product -> product.getId() == productId)
                .findFirst()
                .orElse(null);
    }

    public Product createProduct(Product product) {
        products.add(product);
        return product;
    }

    public Product updateProduct(long productId, Product updatedProduct) {
        Product existingProduct = getProductById(productId);
        if (existingProduct != null) {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCount(updatedProduct.getCount());
        }
        return existingProduct;
    }

    public void deleteProduct(long productId) {
        products.removeIf(product -> product.getId() == productId);
    }
}
