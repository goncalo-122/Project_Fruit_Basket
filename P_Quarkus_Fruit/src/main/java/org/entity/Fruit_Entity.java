package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Fruit_Entity extends PanacheEntity {
    @JsonIgnore
    public Long id;

    public Long fruitId;
    @NotBlank(message = "Can not be empty")
    public String name;
    @Min(message = "Quantity can not be less than 0", value = 0)
    public int quantity;
}