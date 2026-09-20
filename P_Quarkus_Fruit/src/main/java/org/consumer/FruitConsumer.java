package org.consumer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.entity.FruitEntity;
import org.repository.FruitRepository;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.io.StringReader;

@ApplicationScoped
public class FruitConsumer {

    @Inject
    FruitRepository repo;

    @Incoming("basket-events-in")
    @Transactional
    public void consumeBasketEvent(String json) {

        JsonObject obj = Json.createReader(new StringReader(json)).readObject();
        String action = obj.getString("action");

        Long fruitId = obj.getJsonNumber("fruitId").longValue();
        int quantity = obj.getJsonNumber("quantity").intValue();

        FruitEntity fruit = repo.findByFruitId(fruitId);
        if (fruit == null) {
            throw new IllegalArgumentException("Fruit not found");
        }

        switch (action) {
            case "REMOVE_STOCK" -> {
                if (fruit.quantity < quantity) {
                    throw new IllegalArgumentException("Not enough stock");
                }
                fruit.quantity -= quantity;
            }
            case "ADD_STOCK" -> fruit.quantity += quantity;
            default -> {
                return;
            }

        }

    }
    }
