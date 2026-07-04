package service;

import entity.Fruit_Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Fruit_Repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Fruit_ServiceTest {

    Fruit_Service service;
    Fruit_Repository repo;

    @BeforeEach
    void setup() {
        repo = mock(Fruit_Repository.class);
        service = new Fruit_Service();
        service.fruitRepository = repo;
    }

    // -----------------------------
    // addFruits() B.C.S
    // -----------------------------
    @Test
    void testAddFruits_success() {
        Fruit_Entity f1 = new Fruit_Entity();
        f1.fruitId = 1L;
        f1.name = "Apple";
        f1.quantity = 10;

        Fruit_Entity f2 = new Fruit_Entity();
        f2.fruitId = 2L;
        f2.name = "Banana";
        f2.quantity = 20;

        when(repo.findById(1L)).thenReturn(null);
        when(repo.findById(2L)).thenReturn(null);

        service.addFruits(List.of(f1, f2));

        verify(repo).persist(f1);
        verify(repo).persist(f2);
    }

    // -----------------------------
    // addFruits() W.C.S
    // -----------------------------
    @Test
    void testAddFruits_duplicateId() {
        Fruit_Entity f1 = new Fruit_Entity();
        f1.fruitId = 1L;

        when(repo.findById(1L)).thenReturn(f1);

        assertThrows(IllegalArgumentException.class,
                () -> service.addFruits(List.of(f1)));
    }

    // -----------------------------
    // getFruits() B.C.S
    // -----------------------------
    @Test
    void testGetFruits_success() {
        Fruit_Entity f = new Fruit_Entity();
        f.fruitId = 1L;

        when(repo.listAll()).thenReturn(List.of(f));

        List<Fruit_Entity> result = service.getFruits();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).fruitId);
    }

    // -----------------------------
    // getFruits() W.C.S
    // -----------------------------
    @Test
    void testGetFruits_emptyList() {
        when(repo.listAll()).thenReturn(List.of());

        List<Fruit_Entity> result = service.getFruits();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -----------------------------
    // getFruit() B.C.S
    // -----------------------------
    @Test
    void testGetFruit_success() {
        long idToGet = 5L;

        Fruit_Entity f = new Fruit_Entity();
        f.fruitId = idToGet;

        when(repo.findById(idToGet)).thenReturn(f);

        Fruit_Entity result = service.getFruit(idToGet);

        assertNotNull(result);
        assertEquals(idToGet, result.fruitId);
    }

    // -----------------------------
    // getFruit() W.C.S
    // -----------------------------
    @Test
    void testGetFruit_notFound() {
        when(repo.findById(5L)).thenReturn(null);

        Fruit_Entity result = service.getFruit(5L);

        assertNull(result);
    }

    // -----------------------------
    // updateFruit() B.C.S
    // -----------------------------
    @Test
    void testUpdateFruit_success() {
        Fruit_Entity existing = new Fruit_Entity();
        existing.fruitId = 10L;
        existing.name = "Orange";
        existing.quantity = 5;

        Fruit_Entity updated = new Fruit_Entity();
        updated.name = "Apple";
        updated.quantity = 9;

        when(repo.findById(10L)).thenReturn(existing);

        boolean updatedResult = service.updateFruit(10L, updated);

        assertTrue(updatedResult);
        assertEquals("Apple", existing.name);
        assertEquals(9, existing.quantity);
    }

    // -----------------------------
    // updateFruit() W.C.S
    // -----------------------------
    @Test
    void testUpdateFruit_notFound() {
        when(repo.findById(999L)).thenReturn(null);

        Fruit_Entity updated = new Fruit_Entity();
        updated.name = "Kiwi";
        updated.quantity = 10;

        boolean result = service.updateFruit(999L, updated);

        assertFalse(result);
    }

    // -----------------------------
    // deleteFruit() B.C.S
    // -----------------------------
    @Test
    void testDeleteFruit_success() {
        when(repo.deleteById(1L)).thenReturn(true);

        boolean result = service.deleteFruit(1L);

        assertTrue(result);
    }

    // -----------------------------
    // deleteFruit() W.C.S
    // -----------------------------
    @Test
    void testDeleteFruit_notFound() {
        when(repo.deleteById(999L)).thenReturn(false);

        boolean result = service.deleteFruit(999L);

        assertFalse(result);
    }
}
