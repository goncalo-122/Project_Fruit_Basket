package org.service;

import org.repository.BasketRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.client.FruitClient;
import org.dto.FruitDTO;
import org.dto.BasketDTO;
import org.dto.BasketItemDTO;
import org.entity.BasketEntity;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class BasketService {

    @Inject
    BasketRepository basketRepository;

    @RestClient
    FruitClient fruitClient;

    @Transactional
    public BasketItemDTO addItemToBasket(BasketItemDTO dto) {


        if (dto.basketId == null || dto.fruitId == null) {
            throw new IllegalArgumentException("fruitId can not be null");
        }
        FruitDTO remoteFruit = fruitClient.getFruitById(dto.fruitId);


        if (remoteFruit.quantity < dto.quantity) {
            throw new IllegalArgumentException("Stock not available. Available: " + remoteFruit.quantity);
        }

        if (!remoteFruit.name.equalsIgnoreCase(dto.fruitname)) {
            throw new IllegalArgumentException("fruitname doesn´t match -> Name Received" + remoteFruit.name + "Name registered");
        }

        int newStock = remoteFruit.quantity - dto.quantity;


        BasketEntity entity = new BasketEntity();
        entity.basketId = dto.basketId;
        entity.fruitId = dto.fruitId;
        entity.fruitname = remoteFruit.name;
        entity.quantity = dto.quantity;

        FruitDTO frutDTO = new FruitDTO();
        frutDTO.fruitId = remoteFruit.fruitId;
        frutDTO.name = remoteFruit.name;
        frutDTO.quantity = newStock;


        fruitClient.updateFruitQuantity(frutDTO.fruitId, frutDTO);

        dto.fruitname = remoteFruit.name;
        basketRepository.persist(entity);

        return dto;
    }


    public BasketDTO getBasket(Long basketId) {

        List<BasketEntity> entities = basketRepository.list("basketId", basketId);

        BasketDTO dto = new BasketDTO();
        dto.basketId = basketId;

        dto.items = entities.stream().map(e -> {
            BasketItemDTO i = new BasketItemDTO();
            i.basketId = e.basketId;
            i.fruitId = e.fruitId;
            i.fruitname = e.fruitname;
            i.quantity = e.quantity;
            return i;
        }).toList();

        return dto;
    }

    @Transactional
    public boolean deleteBasket(Long id) {

        List<BasketEntity> items = basketRepository.list("basketId", id);

        if (items.isEmpty()) {
            return false;
        }
        for (BasketEntity item : items) {
            FruitDTO fruit = fruitClient.getFruitById(item.fruitId);
            fruit.quantity = fruit.quantity + item.quantity;
            fruitClient.updateFruitQuantity(fruit.fruitId, fruit);
        }
        long deleted = basketRepository.delete("basketId", id);
        return deleted > 0;
    }

    public List<BasketEntity> getBaskets() {
        return basketRepository.listAll();
    }

    @Transactional
    public boolean removeItemFromBasket(Long basketId, Long fruitId) {

        BasketEntity item = basketRepository.find("basketId = ?1 AND fruitId = ?2", basketId, fruitId).firstResult();

        if (item == null) {
            return false;
        }
        FruitDTO fruit = fruitClient.getFruitById(fruitId);
        fruit.quantity += item.quantity;
        fruitClient.updateFruitQuantity(fruitId, fruit);
        long del = basketRepository.delete("basketId = ?1 AND fruitId = ?2", basketId, fruitId);
        return del > 0;
    }

}
