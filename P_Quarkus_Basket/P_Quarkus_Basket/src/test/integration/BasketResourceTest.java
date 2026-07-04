package org.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.dto.BasketItemDTO;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class BasketResourceTest {

    /**
     * Basket-related tests.
     *
     * <p>This group of tests focuses on operations performed directly on baskets,
     * such as retrieving an existing basket, deleting a basket, and validating
     * correct behavior when interacting with basket-level endpoints.</p>
     *
     * <p>The tests ensure that:
     * <ul>
     *   <li>Baskets can be successfully retrieved when they exist.</li>
     *   <li>Deleting an existing basket returns the expected HTTP status code.</li>
     *   <li>Attempting to delete a non-existing basket results in a 404 Not Found.</li>
     *   <li>The API maintains consistent behavior across both valid and invalid scenarios.</li>
     * </ul>
     * </p>
     * <p>
     * NOTE: B.C.S- BEST CASE SCENARIO      W.C.S- WORST CASE SCENARIO
     */


    //B.C.S
    @Test
    void testGetAllBaskets() {
        given().when().get("/basket").then().statusCode(200).body(notNullValue());
    }


    //W.C.S
    @Test
    void testGetBasket_notFound() {
        given().when().get("/basket/999").then().statusCode(404);
    }

    //B.C.S
    @Test
    void testCreateBasket() {

        // Make a Post
        BasketItemDTO dto = new BasketItemDTO();
        dto.basketId = 5L;
        dto.fruitId = 50L;
        dto.fruitname = "Pear";
        dto.quantity = 4;

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/basket")
                .then()
                .statusCode(201);

        // Makes a GET to validate the basket's existence
        given()
                .when().get("/basket/5")
                .then()
                .statusCode(200)
                .body("id", equalTo(5))
                .body("items", not(empty()))
                .body("items.fruitId", hasItem(50));
    }


    //B.C.S
    @Test
    void testDeleteBasket() {


        BasketItemDTO dto = new BasketItemDTO();
        dto.basketId = 2L;
        dto.fruitId = 20L;
        dto.fruitname = "Banana";
        dto.quantity = 5;

        given().contentType(ContentType.JSON).body(dto).when().post("/basket").then().statusCode(201);

        // DELETE — remove it from the basket
        given().when().delete("/basket/delete/2").then().statusCode(204);

        // GET — Validate if it was removed
        given().when().get("/basket/2").then().statusCode(404);
    }

    //W.C.S
    @Test
    void testDeleteNonExistingBasket() {

        given()
                .when().delete("/basket/delete/9999")
                .then()
                .statusCode(404);
    }


    /**
     * Basket item-related tests.
     *
     * <p>This group of tests validates operations involving items inside a basket,
     * including adding items, removing items, and handling invalid item operations.</p>
     *
     * <p>The tests ensure that:
     * <ul>
     *   <li>Items can be added to an existing basket successfully.</li>
     *   <li>Attempting to add an item to a non-existing basket results in a proper error response.</li>
     *   <li>Removing an existing item behaves as expected.</li>
     *   <li>Removing an item that does not exist returns a 404 Not Found.</li>
     *   <li>The API preserves basket integrity when invalid item operations occur.</li>
     * </ul>
     * </p>
     * <p>
     * <p>
     * NOTE: B.C.S- BEST CASE SCENARIO      W.C.S- WORST CASE SCENARIO
     */


    //B.C.S
    @Test
    void testAddItem() {

        BasketItemDTO dto = new BasketItemDTO();
        dto.basketId = 1L;
        dto.fruitId = 10L;
        dto.fruitname = "Apple";
        dto.quantity = 2;

        // POST — Add item
        Long returnedFruitId = given().contentType(ContentType.JSON).body(dto).when().post("/basket").then().statusCode(201).body("fruitId", equalTo(10)).body("quantity", equalTo(2)).extract().path("fruitId");

        // GET — Validate if the item was really saved
        given().when().get("/basket/1").then().statusCode(200).body("items", not(empty())).body("items.fruitId", hasItem(returnedFruitId));
    }

    //W.C.S
    @Test
    void testAddNonExistingFruitTotheBasket() {

        // Fruit doesn´t exist
        BasketItemDTO dto = new BasketItemDTO();
        dto.basketId = 1L;
        dto.fruitId = 9999L;
        dto.fruitname = "GhostFruit";
        dto.quantity = 3;


        given().contentType(ContentType.JSON).body(dto).when().post("/basket").then().statusCode(404).body(containsString("Fruit not found"));


        given().when().get("/basket/1").then().statusCode(200).body("items", anyOf(nullValue(), empty()));
    }

    //W.C.S
    @Test
    void testAddItem_toNonExistingBasket() {

        BasketItemDTO dto = new BasketItemDTO();
        dto.basketId = 9999L; // basket inexistente
        dto.fruitId = 10L;
        dto.fruitname = "Apple";
        dto.quantity = 2;

        given().contentType(ContentType.JSON).body(dto).when().post("/basket").then().statusCode(404).body(containsString("Basket not found"));
    }

    //B.C.S
    @Test
    void testRemoveItem_success() {


        BasketItemDTO dto = new BasketItemDTO();
        dto.basketId = 3L;
        dto.fruitId = 30L;
        dto.fruitname = "Orange";
        dto.quantity = 4;

        given().contentType(ContentType.JSON).body(dto).when().post("/basket").then().statusCode(201);

        given().when().delete("/basket/3/fruit/30").then().statusCode(200).body(containsString("Item removed"));
    }

    //W.C.S
    @Test
    void testRemoveItem_notFound() {
        given().when().delete("/basket/999/fruit/999").then().statusCode(404).body(containsString("Item not found"));
    }


}
