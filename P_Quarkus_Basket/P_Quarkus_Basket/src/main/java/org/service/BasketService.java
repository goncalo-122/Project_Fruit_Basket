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
            throw new IllegalArgumentException(
                    "Stock not available. Available: " + remoteFruit.quantity
            );
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

        long deleted = basketRepository.delete("basketId", id);
        return deleted > 0;




    }
    public List<BasketEntity> getBaskets() {
        return basketRepository.listAll();
    }
}
