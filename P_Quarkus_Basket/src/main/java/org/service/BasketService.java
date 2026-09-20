package org.service;

import org.producer.BasketProducer;
import org.repository.BasketRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.client.FruitClient;
import org.dto.FruitDTO;
import org.dto.BasketDTO;
import org.entity.BasketEntity;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class BasketService {

    @Inject
    BasketRepository basketRepository;

    @RestClient
    FruitClient fruitClient;

    @Inject
    BasketProducer producer;

    public BasketDTO addItemToBasket(BasketDTO dto) {

        if (dto.basketId == null || dto.fruitId == null || dto.quantity <= 0) {
            throw new IllegalArgumentException("basketId, fruitId and quantity cannot be null");
        }

        FruitDTO remoteFruit = fruitClient.getFruitById(dto.fruitId);

        if (remoteFruit.quantity < dto.quantity) {
            throw new IllegalArgumentException("Stock not available. Available: " + remoteFruit.quantity);
        }

        if (!remoteFruit.name.equalsIgnoreCase(dto.fruitname)) {
            throw new IllegalArgumentException(
                    "fruitname doesn't match -> Received: " + dto.fruitname +
                            ", Expected: " + remoteFruit.name
            );
        }

        saveBasketItem(dto.basketId, dto.fruitId, dto.fruitname, dto.quantity);

        producer.sendRemoveStockEvent(dto.fruitId, dto.quantity);

        return dto;
    }



    @Transactional
    protected void saveBasketItem(Long basketId, Long fruitId, String fruitname, int quantity) {
        BasketEntity entity = new BasketEntity();
        entity.basketId = basketId;
        entity.fruitId = fruitId;
        entity.fruitname = fruitname;
        entity.quantity = quantity;

        basketRepository.persist(entity);
    }


    public List<BasketEntity> getBasket(Long basketId) {

        List<BasketEntity> entities = basketRepository.list("basketId", basketId);

        if (entities.isEmpty()) {
            throw new WebApplicationException("Basket not found", Response.Status.NOT_FOUND);
        }

        return entities;
    }


    @Transactional
    public boolean deleteBasket(Long id) {

        List<BasketEntity> items = basketRepository.list("basketId", id);

        for (BasketEntity item : items) {
            producer.sendAddStockEvent(item.fruitId, item.quantity);
        }

        long deleted = basketRepository.delete("basketId", id);
        return deleted > 0;
    }

    public List<BasketEntity> getBaskets() {
        return basketRepository.listAll();
    }

    @Transactional
    public boolean removeItemFromBasket(Long basketId, Long fruitId) {

        BasketEntity item = basketRepository
                .find("basketId = ?1 AND fruitId = ?2", basketId, fruitId)
                .firstResult();

        if (item == null) {
            return false;
        }

        producer.sendAddStockEvent(fruitId, item.quantity);

        long del = basketRepository.delete("basketId = ?1 AND fruitId = ?2", basketId, fruitId);
        return del > 0;
    }
}
