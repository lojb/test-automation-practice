package api.users;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.util.GlobalTestSetup;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PasswordTest extends GlobalTestSetup {

    @Test
    public void testForgotPasswordSuccess() {

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().all()
                .when()
                .post("/forgot-password");
                //.then()
                //.statusCode(200)
                //.body("message", equalTo("Password reset link successfully sent to " + body.get("email") + ". Please verify by clicking on the given link"));

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: \n" + response.getBody().prettyPrint());
    }

    @Test
    public void testForgotPasswordEmptyEmail() {
        Map<String, String> emptyBody = new HashMap<>();

        given()
                .contentType(ContentType.JSON)
                .body(emptyBody)
                .when()
                .post("/forgot-password")
                .then()
                .statusCode(400)
                .body("message", equalTo("A valid email address is required"));
    }

    @Test
    public void testVerifyResetTokenSuccess() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", "asd");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().all()
                .when()
                .post("/users/verify-reset-password-token");

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: \n" + response.getBody().prettyPrint());
    }
}
