package integration;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FruitResourceTest {

    private static Long mangoId;

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
                .statusCode(CREATED.getStatusCode());

        Object rawId = given()
                .when()
                .get("/fruit")
                .then()
                .statusCode(OK.getStatusCode())
                .extract().path("[0].fruitId");

        mangoId = Long.valueOf(rawId.toString());
    }

    @Test
    @Order(2)
    public void testAddFruits_WCS() {

        String chaoticJson = """
        [
            {
                "fruitId": -1,
                "name": "",
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

    @Test
    @Order(3)
    public void testGetFruits() {
        given()
                .when().get("/fruit")
                .then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    @Order(4)
    public void testGetFruit_BCS() {
        given()
                .pathParam("id", mangoId)
                .when()
                .get("/fruit/{id}")
                .then()
                .statusCode(OK.getStatusCode());
    }

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

    @Test
    @Order(6)
    public void testUpdateFruit_BCS() {

        String updatedBody = """
            {"quantity": 20}
        """;

        given()
                .contentType("application/json")
                .body(updatedBody)
                .pathParam("id", mangoId)
                .when()
                .put("/fruit/{id}/quantity")
                .then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    @Order(7)
    public void testUpdateFruit_WCS() {

        String updatedBodyWrong = """
            {"quantity": -20}
        """;

        given()
                .contentType("application/json")
                .body(updatedBodyWrong)
                .pathParam("id", 0)
                .when()
                .put("/fruit/{id}/quantity")
                .then()
                .statusCode(BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(8)
    public void testDeleteFruit_BCS() {
        given()
                .pathParam("id", mangoId)
                .when()
                .delete("/fruit/delete/{id}")
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

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
