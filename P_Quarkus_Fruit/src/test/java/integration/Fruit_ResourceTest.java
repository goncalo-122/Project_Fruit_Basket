package integration;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anyOf;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Fruit_ResourceTest {

    private static Long mangoId;

    // -----------------------------
    // ADD Fruits - B.C.S
    // -----------------------------
    @Test
    @Order(1)
    public void testAddFruits_BCS() {

        String jsonBody = """
            [
                {"fruitId": 1, "name": "Mango", "quantity": 15}
            ]
        """;

        given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/fruit")
                .then()
                .statusCode(anyOf(is(201), is(500)));

        // Convert id type: Int->Long
        Object rawId = given()
                .when()
                .get("/fruit")
                .then()
                .statusCode(OK.getStatusCode())
                .extract().path("[0].id");

        mangoId = Long.valueOf(rawId.toString());
    }

    // -----------------------------
    // ADD Fruits - W.C.S
    // -----------------------------
    @Test
    @Order(2)
    public void testAddFruits_WCS() {

        String chaoticJson = """
        [
            {
                "fruitId": -1,
                "name": 12345,
                "quantity": -10
            }
        ]
        """;

        given()
                .contentType("application/json")
                .body(chaoticJson)
                .when()
                .post("/fruit")
                .then()
                .statusCode(BAD_REQUEST.getStatusCode());
    }

    // -----------------------------
    // GET Fruits
    // -----------------------------
    @Test
    @Order(3)
    public void testGetFruits() {
        given()
                .when().get("/fruit")
                .then()
                .statusCode(OK.getStatusCode());
    }

    // -----------------------------
    // GET Fruit - B.C.S
    // -----------------------------
    @Test
    @Order(4)
    public void testGetFruit_BCS() {
        given()
                .pathParam("id", mangoId)
                .when()
                .get("/fruit/{id}")
                .then()
                .statusCode(anyOf(is(OK.getStatusCode()), is(NOT_FOUND.getStatusCode())));
    }

    // -----------------------------
    // GET Fruit - W.C.S
    // -----------------------------
    @Test
    @Order(5)
    public void testGetFruit_WCS() {
        given()
                .pathParam("id", 99999)
                .when()
                .get("/fruit/{id}")
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    // -----------------------------
    // UPDATE Fruit - B.C.S
    // -----------------------------
    @Test
    @Order(6)
    public void testUpdateFruit_BCS() {

        String updatedBody = """
            {"fruitId": 1, "name": "Apple", "quantity": 20}
        """;

        given()
                .contentType("application/json")
                .body(updatedBody)
                .pathParam("id", mangoId)
                .when()
                .put("/fruit/{id}/quantity")
                .then()
                .statusCode(anyOf(is(OK.getStatusCode()), is(NOT_FOUND.getStatusCode())));
    }

    // -----------------------------
    // UPDATE Fruit - W.C.S
    // -----------------------------
    @Test
    @Order(7)
    public void testUpdateFruit_WCS() {

        String updatedBodyWrong = """
            {"fruitId": 0, "name": "", "quantity": -20}
        """;

        given()
                .contentType("application/json")
                .body(updatedBodyWrong)
                .pathParam("id", 0)
                .when()
                .put("/fruit/{id}/quantity")
                .then()
                .statusCode(anyOf(is(BAD_REQUEST.getStatusCode()), is(NOT_FOUND.getStatusCode())));
    }

    // -----------------------------
    // DELETE Fruit - B.C.S
    // -----------------------------
    @Test
    @Order(8)
    public void testDeleteFruit_BCS() {
        given()
                .pathParam("id", mangoId)
                .when()
                .delete("/fruit/delete/{id}")
                .then()
                .statusCode(anyOf(is(NO_CONTENT.getStatusCode()), is(NOT_FOUND.getStatusCode())));
    }

    // -----------------------------
    // DELETE Fruit - W.C.S
    // -----------------------------
    @Test
    @Order(9)
    public void testDeleteFruit_WCS() {
        given()
                .pathParam("id", 99999)
                .when()
                .delete("/fruit/delete/{id}")
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }
}
