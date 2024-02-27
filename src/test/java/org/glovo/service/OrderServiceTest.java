package org.glovo.service;

import org.glovo.entity.GlovoOrder;
import org.glovo.entity.Product;
import org.glovo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderServiceTest {

    private Product product1;
    private Product product2;
    private Product product3;
    private List<Product> products;

    public OrderServiceTest() {
        product1 = new Product(1, "apple", 12.12, 2);
        product2 = new Product(2, "nuts", 12, 2);
        product3 = new Product(3, "banana", 12, 2);

        products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
    }

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService = new OrderService();
    @Captor
    private ArgumentCaptor<GlovoOrder> orderCaptor;

    @Test
    public void testGetAllOrders() {

        GlovoOrder order1 = new GlovoOrder(1, products, 36.12, 3, new Date(), null);
        GlovoOrder order2 = new GlovoOrder(2, products, 36.12, 3, new Date(), null);
        GlovoOrder order3 = new GlovoOrder(3, products, 36.12, 3, new Date(), null);

        ArrayList<GlovoOrder> expectedOrders = new ArrayList<>();
        expectedOrders.add(order1);
        expectedOrders.add(order2);
        expectedOrders.add(order3);

        when(orderRepository.findAll()).thenReturn(expectedOrders);

        List<GlovoOrder> actualOrders = orderService.getAllOrders();

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void testGetOrderById() {

        GlovoOrder order1 = new GlovoOrder(1, products, 36.12, 3, new Date(), null);
        GlovoOrder order2 = new GlovoOrder(2, products, 36.12, 3, new Date(), null);
        GlovoOrder order3 = new GlovoOrder(3, products, 36.12, 3, new Date(), null);

        ArrayList<GlovoOrder> expectedOrders = new ArrayList<>();
        expectedOrders.add(order1);
        expectedOrders.add(order2);
        expectedOrders.add(order3);

        Mockito.when(orderRepository.getReferenceById(1L)).thenReturn(expectedOrders.get(1));

        GlovoOrder actualIdOrder = orderService.getById(1L);
        assertEquals(expectedOrders.get(1), actualIdOrder);
    }

    @Test
    void testCreateOrder() {
        GlovoOrder orderOriginal = new GlovoOrder(0, products, 0, 0, null, null);
        GlovoOrder expectedOrder = new GlovoOrder(1, products, 36.12, 3, new Date(), null);

        Mockito.when(orderRepository.save(any(GlovoOrder.class))).thenReturn(expectedOrder);
        GlovoOrder actualOrder = orderService.create(orderOriginal);
        assertEquals(expectedOrder, actualOrder);

        verify(orderRepository).save(orderCaptor.capture());
        GlovoOrder orderCalculated = orderCaptor.getValue();
        assertEquals(36.12, orderCalculated.getTotalPrice());
        assertEquals(3, orderCalculated.getProductQuantity());
        assertNotNull(orderCalculated.getCreation());
    }

    @Test
    public void testUpdateOrder() {
        ArrayList<Product> products2 = new ArrayList<>();
        products2.add(product1);
        products2.add(product2);

        GlovoOrder existingOrder = new GlovoOrder(1L, products, 36.12, 3, null, null);
        GlovoOrder expectedOrder = new GlovoOrder(1L, products2, 24.12, 2, null, null);

        when(orderRepository.getById(1L)).thenReturn(existingOrder);
        when(orderRepository.save(any(GlovoOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GlovoOrder actualOrder = orderService.update(1, expectedOrder);

        assertEquals(expectedOrder, actualOrder);
        verify(orderRepository, times(1)).getById(1L);
        verify(orderRepository, times(1)).save(any(GlovoOrder.class));
    }

    @Test
    public  void testDeleteOrder() {
        orderService.delete(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddProductToOrder() {
        Product product = new Product(1, "apple", 12.12, 2);
        ArrayList<Product> products = new ArrayList<>();
        products.add(product);

        GlovoOrder expectedOrder = new GlovoOrder(1L, products, 36.12, 3, null, null);

        when(orderRepository.getReferenceById(1L)).thenReturn(expectedOrder);
        when(productService.getProduct(1)).thenReturn(product);
        when(orderRepository.save(expectedOrder)).thenReturn(expectedOrder);

        GlovoOrder actualOrder = orderService.addProduct(1, 1);

        assertEquals(expectedOrder, actualOrder);
        verify(orderRepository, times(1)).getReferenceById(1L);
        verify(productService, times(1)).getProduct(1);
        verify(orderRepository, times(1)).save(expectedOrder);
    }

    @Test
    public void testRemoveProductFromOrder() {
        GlovoOrder expectedOrder = new GlovoOrder(1L, products, 36.12, 3, null, null);

        when(orderRepository.getReferenceById(1L)).thenReturn(expectedOrder);
        when(orderRepository.save(expectedOrder)).thenReturn(expectedOrder);

        GlovoOrder actualOrder = orderService.removeProduct(1, 1);

        assertEquals(expectedOrder, actualOrder);
        verify(orderRepository, times(1)).getReferenceById(1L);
        verify(orderRepository, times(1)).save(expectedOrder);
    }
}
