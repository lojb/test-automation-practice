package api.health;

import org.example.util.GlobalTestSetup;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class HealthTest extends GlobalTestSetup {

    @Test
    public void testHealth() {

        given()
                .when()
                .get("/health-check")
                .then()
                .statusCode(200)
                .body("message", equalTo("Notes API is Running"));
    }

}
