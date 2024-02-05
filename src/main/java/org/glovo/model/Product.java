package org.glovo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private double price;
    private int count;
}
