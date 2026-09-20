package unit;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.entity.FruitEntity;
import org.junit.jupiter.api.Test;
import org.repository.FruitRepository;
import org.service.FruitService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class FruitServiceTest {

    @Inject
    FruitService service;

    @InjectMock
    FruitRepository repo;

    // ADD FRUITS
    @Test
    void testAddFruits_success() {
        FruitEntity f1 = new FruitEntity();
        f1.fruitId = 1L;
        f1.name = "Apple";
        f1.quantity = 10;

        FruitEntity f2 = new FruitEntity();
        f2.fruitId = 2L;
        f2.name = "Banana";
        f2.quantity = 20;

        when(repo.findByFruitId(1L)).thenReturn(null);
        when(repo.findByFruitId(2L)).thenReturn(null);

        service.addFruits(List.of(f1, f2));

        verify(repo).persist(f1);
        verify(repo).persist(f2);
    }

    @Test
    void testAddFruits_duplicateId() {
        FruitEntity f1 = new FruitEntity();
        f1.fruitId = 1L;

        when(repo.findByFruitId(1L)).thenReturn(f1);

        assertThrows(IllegalArgumentException.class,
                () -> service.addFruits(List.of(f1)));
    }

    // GET FRUITS
    @Test
    void testGetFruits_success() {
        FruitEntity f = new FruitEntity();
        f.fruitId = 1L;

        when(repo.listAll()).thenReturn(List.of(f));

        List<FruitEntity> result = service.getFruits();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).fruitId);
    }

    @Test
    void testGetFruit_success() {
        FruitEntity f = new FruitEntity();
        f.fruitId = 5L;

        when(repo.findByFruitId(5L)).thenReturn(f);

        FruitEntity result = service.getFruit(5L);

        assertNotNull(result);
        assertEquals(5L, result.fruitId);
    }

    @Test
    void testGetFruit_notFound() {
        when(repo.findByFruitId(5L)).thenReturn(null);

        FruitEntity result = service.getFruit(5L);

        assertNull(result);
    }

    // UPDATE STOCK
    @Test
    void testUpdateFruitStock_success() {
        FruitEntity existing = new FruitEntity();
        existing.fruitId = 10L;
        existing.quantity = 5;

        when(repo.findByFruitId(10L)).thenReturn(existing);

        boolean result = service.updateFruitStock(10L, 20);

        assertTrue(result);
        assertEquals(20, existing.quantity);
        verify(repo).persist(existing);
    }

    @Test
    void testUpdateFruitStock_notFound() {
        when(repo.findByFruitId(999L)).thenReturn(null);

        boolean result = service.updateFruitStock(999L, 20);

        assertFalse(result);
    }

    @Test
    void testUpdateFruitStock_negative() {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateFruitStock(10L, -5));
    }

    // DELETE FRUIT
    @Test
    void testDeleteFruit_success() {
        FruitEntity f = new FruitEntity();
        f.fruitId = 1L;

        when(repo.findByFruitId(1L)).thenReturn(f);
        when(repo.deleteById(1L)).thenReturn(true);

        boolean result = service.deleteFruit(1L);

        assertTrue(result);
    }

    @Test
    void testDeleteFruit_notFound() {
        when(repo.findByFruitId(999L)).thenReturn(null);

        boolean result = service.deleteFruit(999L);

        assertFalse(result);
    }
}
