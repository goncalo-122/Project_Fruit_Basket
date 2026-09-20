package org.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FruitEntity {

    @Id
    public Long fruitId;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public int quantity;
}
