package org.glovo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class Order {
    private String id;
    private List<Product> products;
    private int totalPrice;
    private int productQuantity;
    private Date creation;
    private Date modification;
}
