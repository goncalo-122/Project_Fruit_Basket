package service;

import entity.Fruit_Entity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.PathParam;
import repository.Fruit_Repository;

import java.util.List;


@ApplicationScoped
public class Fruit_Service {

    @Inject
    Fruit_Repository fruitRepository;

    @Transactional
    public void addFruits(@Valid List<Fruit_Entity> fruits) {

        for (Fruit_Entity fruit : fruits) {
            if (fruitRepository.findById(fruit.fruitId) != null) {
                throw new IllegalArgumentException("Fruit with ID " + fruit.fruitId + " already exists");
            }
            fruitRepository.persist(fruit);
        }


    }


    public List<Fruit_Entity> getFruits() {
        return fruitRepository.listAll();
    }

    public Fruit_Entity getFruit(Long id) {
        return fruitRepository.findById(id);
    }

    @Transactional
    public boolean updateFruit(Long id, @Valid Fruit_Entity updatedFruit) {
        Fruit_Entity fruitFind = fruitRepository.findById(id);
        if (fruitFind == null) {
            return false;
        }
        fruitFind.name = updatedFruit.name;
        fruitFind.quantity = updatedFruit.quantity;
        return true;
    }

    @Transactional
    public boolean deleteFruit(Long id) {
        return fruitRepository.deleteById(id);
    }

}


