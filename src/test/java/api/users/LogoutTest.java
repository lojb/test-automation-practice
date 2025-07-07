package api.users;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.util.GlobalTestSetup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LogoutTest extends GlobalTestSetup {

    @DataProvider(name = "logoutTestCases")
    public Object[][] logoutTestCases() {
        return new Object[][]{
                // token, expectedStatusCode, expectedMessage
                { null,             401, "No authentication token specified in x-auth-token header" },
                { "notavalidtoken", 401, "Access token is not valid or has expired, you will need to login" },
                { token,            200, "User has been successfully logged out" }
        };
    }

    @Test(dataProvider = "logoutTestCases")
    public void testLogoutUser(String providedToken, int expectedStatusCode, String expectedMessage) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON);

        if (providedToken != null) {
            request.header("x-auth-token", providedToken);
        }

        request
                .when()
                .delete("/users/logout")
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }

}
