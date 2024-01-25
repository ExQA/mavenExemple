package org.glovo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private long id;
    private String name;
    private double price;
    private int count;
}
