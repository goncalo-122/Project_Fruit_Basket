package org.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.entity.FruitEntity;
import org.repository.FruitRepository;

import java.util.List;

@ApplicationScoped
public class FruitService {

    @Inject
    FruitRepository repo;

    public void addFruits(List<FruitEntity> fruits) {

        for (FruitEntity f : fruits) {

            if (f.fruitId == null || f.fruitId <= 0)
                throw new IllegalArgumentException("Invalid fruitId");

            if (f.name == null || f.name.trim().isEmpty())
                throw new IllegalArgumentException("Invalid name");

            if (f.quantity < 0)
                throw new IllegalArgumentException("Invalid quantity");

            FruitEntity existing = repo.findByFruitId(f.fruitId);

            if (existing != null)
                throw new IllegalArgumentException("Duplicate fruitId");

            repo.persist(f);
        }
    }

    public List<FruitEntity> getFruits() {
        return repo.listAll();
    }

    public FruitEntity getFruit(Long id) {
        return repo.findByFruitId(id);
    }

    public boolean updateFruitStock(Long id, int quantity) {

        if (quantity < 0)
            throw new IllegalArgumentException("Quantity cannot be negative");

        FruitEntity existing = repo.findByFruitId(id);

        if (existing == null)
            return false;

        existing.quantity = quantity;
        repo.persist(existing);

        return true;
    }

    public boolean deleteFruit(Long id) {

        FruitEntity existing = repo.findByFruitId(id);

        if (existing == null)
            return false;

        return repo.deleteById(id);
    }
}
