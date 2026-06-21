package org.entity;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.lang.Long;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class BasketEntity extends PanacheEntity {

    @JsonIgnore
    public Long id;

    public Long basketId;
    @Min(message = "Fruit Id can not be less than 0", value = 0)
    public Long fruitId;
    @NotBlank(message = "Can not be empty")
    public String fruitname;
    @Min(message = "Quantity can not be less than 0", value = 0)
    public int quantity;

}
