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

public class ProfileTest extends GlobalTestSetup {

    @DataProvider(name = "getProfileTestCases")
    public Object[][] getProfileTestCases() {
        return new Object[][]{
                // token, expectedStatusCode, expectedMessage
                { null,             401, "No authentication token specified in x-auth-token header" },
                { "notavalidtoken", 401, "Access token is not valid or has expired, you will need to login" },
                { token,            200, "Profile successful" }
        };
    }

    @DataProvider(name = "patchProfileTestCases")
    public Object[][] patchProfileTestCases() {
        Map<String, String> validBody = new HashMap<>();
        validBody.put("name", "updatedName");

        Map<String, String> missingNameBody = new HashMap<>();
        missingNameBody.put("company", "updatedCompany");

        Map<String, String> unauthorizedBody = new HashMap<>();
        unauthorizedBody.put("company", "updatedCompany");

        return new Object[][]{
                // body, token, expectedStatus, expectedMessage
                { validBody, token, 200, "Profile updated successful" },
                { missingNameBody, token, 400, "User name must be between 4 and 30 characters" },
                { unauthorizedBody, null, 401, "No authentication token specified in x-auth-token header" }
        };
    }

    @Test(dataProvider = "getProfileTestCases")
    public void testGetProfile(String providedToken, int expectedStatusCode, String expectedMessage) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON);

        if (providedToken != null) {
            request.header("x-auth-token", providedToken);
        }

        request
                .when()
                .get("/users/profile")
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }

    @Test(dataProvider = "patchProfileTestCases")
    public void testPatchProfile(Map<String, String> requestBody, String providedToken, int expectedStatus, String expectedMessage) {
        var request = given()
                .contentType(ContentType.JSON)
                .body(requestBody);

        if (providedToken != null) {
            request.header("x-auth-token", providedToken);
        }

        request
                .when()
                .patch("/users/profile")
                .then()
                .statusCode(expectedStatus)
                .body("message", equalTo(expectedMessage));
    }
}
