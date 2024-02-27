package org.glovo.service;

import org.glovo.entity.Product;
import org.glovo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService = new ProductService();

    @Test
    void testGetAllProducts() {
        Product product1 = new Product(1, "apple", 12.12, 2);
        Product product2 = new Product(2, "banana", 12.12, 2);
        Product product3 = new Product(3, "nuts", 12.12, 2);

        ArrayList<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(product1);
        expectedProducts.add(product2);
        expectedProducts.add(product3);

        Mockito.when(productRepository.findAll()).thenReturn(expectedProducts);
        List<Product> actualResult = productService.getAllProducts();
        assertEquals(expectedProducts, actualResult);
    }

    @Test
    public void testGetProduct() {
        Product expectedProduct = new Product(1, "apple", 12.12, 2);

        when(productRepository.getReferenceById(1L)).thenReturn(expectedProduct);

        Product actualProduct = productService.getProduct(1);
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product(1, "apple", 12.12, 2);
        Product expectedProduct = new Product(1, "apple", 12.12, 2);

        when(productRepository.save(product)).thenReturn(expectedProduct);

        Product actualProduct = productService.create(product);
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void testDeleteProduct() {
        productService.delete(1);

        verify(productRepository, times(1)).deleteById(1L);
    }
}