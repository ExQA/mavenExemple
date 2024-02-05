package org.glovo.controller;

import lombok.AllArgsConstructor;
import org.glovo.model.Product;
import org.glovo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping()
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductId(@PathVariable long productId) {
        return productService.getProduct(productId);
    }

    @PostMapping("/add-product")
    public Product saveProduct(@RequestBody Product product) {
        return productService.create(product);
    }
}
