package api.notes;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.util.GlobalTestSetup;
import org.example.util.NotesUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UpdateNoteTest extends GlobalTestSetup {

    @DataProvider(name = "updateNodeTestCases")
    public Object[][] updateNodeTestCases() {
        String noteId = NotesUtil.createNewNode(token);
        return new Object[][]{
                // token, body, expectedStatusCode, expectedMessage
                {null, getValidPutBody(NotesUtil.createNewNode(token)), 401, "No authentication token specified in x-auth-token header"},
                {"notavalidtoken", getValidPutBody(NotesUtil.createNewNode(token)), 401, "Access token is not valid or has expired, you will need to login"},
                {token, getValidPutBody("invalidId"), 400, "Note ID must be a valid ID"},
                {token, getValidPutBody(NotesUtil.createNewNode(token)), 200, "Note successfully Updated"},
        };
    }

    @DataProvider(name = "updateCompletedStatusTestCases")
    public Object[][] updateCompletedStatusTestCases() {
        String noteId = NotesUtil.createNewNode(token);
        return new Object[][]{
                // token, body, expectedStatusCode, expectedMessage
                {null, getValidPatchBody(NotesUtil.createNewNode(token)), 401, "No authentication token specified in x-auth-token header"},
                {"notavalidtoken", getValidPatchBody(NotesUtil.createNewNode(token)), 401, "Access token is not valid or has expired, you will need to login"},
                {token, getValidPatchBody("invalidId"), 400, "Note ID must be a valid ID"},
                {token, getValidPatchBody(NotesUtil.createNewNode(token)), 200, "Note successfully Updated"},
        };
    }

    @Test(dataProvider = "updateNodeTestCases")
    public void testupdateNode(String providedToken, Map<String, String> body, int expectedStatusCode, String expectedMessage) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON);

        if (providedToken != null) {
            request.header("x-auth-token", providedToken);
        }

        request
                .when()
                .body(body)
                .put("/notes/" + body.get("id"))
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }

    @Test(dataProvider = "updateCompletedStatusTestCases")
    public void testupdateCompletedStatus(String providedToken, Map<String, String> body, int expectedStatusCode, String expectedMessage) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON);

        if (providedToken != null) {
            request.header("x-auth-token", providedToken);
        }

        request
                .when()
                .body(body)
                .patch("/notes/" + body.get("id"))
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }

    private Map<String, String> getValidPutBody(String id) {
        return Map.of(
                "id", id,
                "title", "validTitle",
                "description", "validDesc",
                "category", "Home",
                "completed", "false"
        );
    }

    private Map<String, String> getValidPatchBody(String id) {
        return Map.of(
                "id", id,
                "completed", "true"
        );
    }
}
