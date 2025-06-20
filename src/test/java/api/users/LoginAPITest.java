package api.users;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.util.GlobalTestSetup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginAPITest extends GlobalTestSetup {

    @DataProvider(name = "loginTestCases")
    public Object[][] loginTestCases() {
        Map<String, String> validBody = new HashMap<>(body);
        Map<String, String> invalidPasswordBody = new HashMap<>(body);
        invalidPasswordBody.put("password", "balazstestwrongpw");

        return new Object[][]{
                // requestBody, expectedStatusCode, expectedMessage
                { validBody, 200, "Login successful" },
                { invalidPasswordBody, 401, "Incorrect email address or password" }
        };
    }

    @Test(dataProvider = "loginTestCases")
    public void testLogin(Map<String, Object> requestBody, int expectedStatusCode, String expectedMessage) {
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/users/login")
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }
}
