package org.glovo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class GlovoOrder {
    @Id
    @GeneratedValue
    private long id;
    @ManyToMany
    private List<Product> products;
    private double totalPrice;
    private int productQuantity;
    private Date creation;
    private Date modification;
}
