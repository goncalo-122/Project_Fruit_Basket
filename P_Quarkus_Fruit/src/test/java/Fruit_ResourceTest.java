import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Fruit_ResourceTest {
    private static Integer mangoId;

    //Test-ADD_Fruits B.C.S
    @Test
    @Order(1)
    public void testAddFruits_BestCaseScenario(){

        String jsonBody = """
            [
                {"name": "Mango", "quantity": 15},
                {"name": "Kiwi", "quantity": 30}
            ]
        """;

        given()
            .contentType("application/json")
            .body(jsonBody)
        .when()
            .post("/fruit")
        .then()
            .statusCode(CREATED.getStatusCode())

                .body("size()", is(2))
                .body("[0].name", is("Mango"))
                .body("[0].quantity", is(15))
                .body("[1].name", is("Kiwi"))
                .body("[1].quantity", is(30));

        mangoId=given()
                .when()
                .get("/fruit")
                .then()
                .body("[0].name", is("Mango"))
                .extract().path("[0].id");
    }


    //Test-ADD_Fruits W.C.S
    @Test
    @Order(2)
    public void testAddFruits_WorstCaseScenario(){

        String chaoticJson = """
        [
            {
                "id": -1,
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


    //Test-Get-Fruits
    @Test
    @Order(3)
    public void testGetFruitsEndPoint(){
        given()
                .when().get("/fruit")
                .then()
                .statusCode(OK.getStatusCode());
    }

    //Test-Get-Fruit-B.C.S
    @Test
    @Order(4)
    public void testGetFruit_BestCase(){
        given()
            .pathParam("id",mangoId)
        .when()
            .get("/fruit/{id}")
        .then()
            .statusCode(OK.getStatusCode())
            .body("name", is("Mango"));
    }


    //Test-Get-Fruit-W.C.S
    @Test
    @Order(5)
    public void testGetFruit_WorstCase(){

        given()
                .pathParam("id",1000)
                .when()
                .get("/fruit/{id}")
                .then()
                .statusCode(NOT_FOUND.getStatusCode());

        given()
                .pathParam("id",-1)
                .when()
                .get("/fruit/{id}")
                .then()
                .statusCode(NOT_FOUND.getStatusCode());

    }

    //Test-Update-Fruit-B.C.S
    @Test
    @Order(6)
    public void testUpdateFruit_BestCase() {
        String updatedBody = """
            {"name": "Apple", "quantity":20}
        """;

        given()
                .contentType("application/json")
                .body(updatedBody)
                .pathParam("id", mangoId)
                .when()
                .put("/fruit/update/{id}")
                .then()
                .statusCode(OK.getStatusCode());
    }

    //Test-Update-Fruit-W.C.S
    @Test
    @Order(7)
    public void testUpdateFruit_WorstCase() {
        String updatedBodyWrong = """
            {"name": "123*", "quantity":-20}
        """;

        given()
                .contentType("application/json")
                .body(updatedBodyWrong)
                .pathParam("id", 0)
                .when()
                .put("/fruit/update/{id}")
                .then()
                .statusCode(BAD_REQUEST.getStatusCode());
    }
    //Test-Delete-Fruit-B.C.S
    @Test
    @Order(8)
    public void testDeleteFruit_BestCase() {
        given()
                .pathParam("id", mangoId)
                .when()
                .delete("/fruit/delete/{id}")
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    //Test-Delete-Fruit-W.C.S
    @Test
    @Order(9)
    public void testDeleteFruit_WorstCase() {
        given()
                .pathParam("id", 999)
                .when()
                .delete("/fruit/delete/{id}")
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }
}
