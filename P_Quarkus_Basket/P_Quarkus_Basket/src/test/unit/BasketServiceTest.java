package org.service;

import org.client.FruitClient;
import org.dto.BasketDTO;
import org.dto.BasketItemDTO;
import org.dto.FruitDTO;
import org.entity.BasketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.repository.BasketRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasketServiceTest {

    BasketService service;
    BasketRepository repo;
    FruitClient fruitClient;

    @BeforeEach
    void setup() {
        repo = mock(BasketRepository.class);
        fruitClient = mock(FruitClient.class);

        service = new BasketService();
        service.basketRepository = repo;
        service.fruitClient = fruitClient;
    }

    // ---------------------------------------------------------
    // addItemToBasket() B.C.S
    // ---------------------------------------------------------
    @Test
    void testAddItemToBasket_success() {
        //item that desires to be added to the basket
        BasketItemDTO dto = new BasketItemDTO();
        dto.basketId = 1L;
        dto.fruitId = 10L;
        dto.fruitname = "Apple";
        dto.quantity = 2;
        //item "came" from  Fruitś Microservice side
        FruitDTO fruit = new FruitDTO();
        fruit.fruitId = 10L;
        fruit.name = "Apple";
        fruit.quantity = 10;

        when(fruitClient.getFruitById(10L)).thenReturn(fruit);

        BasketItemDTO result = service.addItemToBasket(dto);

        assertEquals("Apple", result.fruitname);

        //makes sure that fruit was saved in database
        verify(repo).persist(any(BasketEntity.class));
        //makes sure that quantity of fruit was updated
        verify(fruitClient).updateFruitQuantity(eq(10L), any(FruitDTO.class));
    }


    // ---------------------------------------------------------
    // addItemToBasket() W.C.S - insufficient stock
    // ---------------------------------------------------------
    @Test
    void testAddItemToBasket_insufficientStock() {
        BasketItemDTO dto = new BasketItemDTO();
        dto.basketId = 1L;
        dto.fruitId = 10L;
        dto.fruitname = "Apple";
        dto.quantity = 20;

        FruitDTO fruit = new FruitDTO();
        fruit.fruitId = 10L;
        fruit.name = "Apple";
        fruit.quantity = 5;

        when(fruitClient.getFruitById(10L)).thenReturn(fruit);

        assertThrows(IllegalArgumentException.class, () -> service.addItemToBasket(dto));
    }

    // ---------------------------------------------------------
    // addItemToBasket() W.C.S - fruitname mismatch
    // ---------------------------------------------------------
    @Test
    void testAddItemToBasket_wrongFruitName() {
        BasketItemDTO dto = new BasketItemDTO();
        dto.basketId = 1L;
        dto.fruitId = 10L;
        dto.fruitname = "Banana"; // errado
        dto.quantity = 2;

        FruitDTO fruit = new FruitDTO();
        fruit.fruitId = 10L;
        fruit.name = "Apple";
        fruit.quantity = 10;

        when(fruitClient.getFruitById(10L)).thenReturn(fruit);

        assertThrows(IllegalArgumentException.class, () -> service.addItemToBasket(dto));
    }

    // ---------------------------------------------------------
    // removeItemFromBasket() B.C.S
    // ---------------------------------------------------------
    @Test
    void testRemoveItemFromBasket_itemExists() {

        BasketEntity entity = new BasketEntity();
        entity.basketId = 1L;
        entity.fruitId = 10L;
        entity.quantity = 3;

        PanacheQuery<BasketEntity> query = mock(PanacheQuery.class);

        when(repo.find("basketId = ?1 AND fruitId = ?2", 1L, 10L))
                .thenReturn(query);

        when(query.firstResult()).thenReturn(entity);

        FruitDTO fruit = new FruitDTO();
        fruit.fruitId = 10L;
        fruit.quantity = 5;

        when(fruitClient.getFruitById(10L)).thenReturn(fruit);
        when(repo.delete("basketId = ?1 AND fruitId = ?2", 1L, 10L)).thenReturn(1L);

        boolean removed = service.removeItemFromBasket(1L, 10L);

        assertTrue(removed);
        assertEquals(8, fruit.quantity);
        verify(fruitClient).updateFruitQuantity(eq(10L), any(FruitDTO.class));
    }

    // ---------------------------------------------------------
    // removeItemFromBasket() W.C.S
    // ---------------------------------------------------------
    @Test
    void testRemoveItemFromBasket_itemNotFound() {

        PanacheQuery<BasketEntity> query = mock(PanacheQuery.class);

        when(repo.find("basketId = ?1 AND fruitId = ?2", 1L, 10L))
                .thenReturn(query);

        when(query.firstResult()).thenReturn(null);

        boolean removed = service.removeItemFromBasket(1L, 10L);

        assertFalse(removed);
    }

    // ---------------------------------------------------------
    // deleteBasket() B.C.S
    // ---------------------------------------------------------
    @Test
    void testDeleteBasket_success() {

        BasketEntity item = new BasketEntity();
        item.basketId = 1L;
        item.fruitId = 10L;
        item.quantity = 3;

        when(repo.list("basketId", 1L)).thenReturn(List.of(item));

        FruitDTO fruit = new FruitDTO();
        fruit.fruitId = 10L;
        fruit.quantity = 5;

        when(fruitClient.getFruitById(10L)).thenReturn(fruit);
        when(repo.delete("basketId", 1L)).thenReturn(1L);

        boolean deleted = service.deleteBasket(1L);

        assertTrue(deleted);
        assertEquals(8, fruit.quantity);
        verify(fruitClient).updateFruitQuantity(eq(10L), any(FruitDTO.class));
    }

    // ---------------------------------------------------------
    // deleteBasket() W.C.S
    // ---------------------------------------------------------
    @Test
    void testDeleteBasket_notFound() {

        when(repo.list("basketId", 999L)).thenReturn(List.of());
        when(repo.delete("basketId", 999L)).thenReturn(0L);

        boolean deleted = service.deleteBasket(999L);

        assertFalse(deleted);
    }

    // ---------------------------------------------------------
    // getBasket() B.C.S
    // ---------------------------------------------------------
    @Test
    void testGetBasket_success() {

        BasketEntity e = new BasketEntity();
        e.basketId = 1L;
        e.fruitId = 10L;
        e.fruitname = "Apple";
        e.quantity = 3;

        when(repo.list("basketId", 1L)).thenReturn(List.of(e));

        BasketDTO dto = service.getBasket(1L);

        assertEquals(1L, dto.basketId);
        assertEquals(1, dto.items.size());
        assertEquals("Apple", dto.items.get(0).fruitname);
    }
}
