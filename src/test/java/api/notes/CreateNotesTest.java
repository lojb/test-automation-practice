package api.notes;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.util.GlobalTestSetup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateNotesTest extends GlobalTestSetup {

    @DataProvider(name = "createNodeTestCases")
    public Object[][] createNodeTestCases() {
        return new Object[][]{
                // token, body, expectedStatusCode, expectedMessage
                {null, getValidBody(), 401, "No authentication token specified in x-auth-token header"},
                {"notavalidtoken", getValidBody(), 401, "Access token is not valid or has expired, you will need to login"},
                {token, getValidBody(), 200, "Note successfully created"},
                {token, getBodyInvalidTitle(), 400, "Title must be between 4 and 100 characters"},
                {token, getBodyInvalidDesc(), 400, "Description must be between 4 and 1000 characters"},
                {token, getBodyMissingCategory(), 400, "Category must be one of the categories: Home, Work, Personal"},
        };
    }

    @Test(dataProvider = "createNodeTestCases")
    public void testCreateNode(String providedToken, Map<String, String> bodyMap, int expectedStatusCode, String expectedMessage) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON);

        if (providedToken != null) {
            request.header("x-auth-token", providedToken);
        }

        request
                .body(bodyMap)
                .when()
                .post("/notes")
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }

    private Map<String, String> getValidBody() {
        return Map.of(
                "title", "validTitle",
                "description", "validDesc",
                "category", "Home"
        );
    }

    private Map<String, String> getBodyInvalidTitle() {
        return Map.of(
                "title", "inv",
                "description", "validDesc",
                "category", "Home"
        );
    }

    private Map<String, String> getBodyInvalidDesc() {
        return Map.of(
                "title", "validTitle",
                "description", "inv",
                "category", "Home"
        );
    }

    private Map<String, String> getBodyMissingCategory() {
        return Map.of(
                "title", "validTitle",
                "description", "validDesc"
        );
    }

}
