package org.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.entity.BasketEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class BasketRepository implements PanacheRepository<BasketEntity> {
}
