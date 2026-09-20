package org.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.entity.FruitEntity;

@ApplicationScoped
public class FruitRepository implements PanacheRepository<FruitEntity> {

    public FruitEntity findByFruitId(Long id) {
        return find("fruitId", id).firstResult();
    }
}
