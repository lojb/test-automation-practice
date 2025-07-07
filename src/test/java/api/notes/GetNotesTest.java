package api.notes;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.util.GlobalTestSetup;
import org.example.util.NotesUtil;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetNotesTest extends GlobalTestSetup {

    @DataProvider(name = "getAllNodesTestCases")
    public Object[][] getAllNodesTestCases() {
        return new Object[][]{
                // token, expectedStatusCode, expectedMessages
                {null, 401, List.of("No authentication token specified in x-auth-token header")},
                {"notavalidtoken", 401, List.of("Access token is not valid or has expired, you will need to login")},
                {token, 200, List.of("No notes found", "Notes successfully retrieved")},
        };
    }

    @DataProvider(name = "getNodeByIdTestCases")
    public Object[][] getNodeByIdTestCases() {
        return new Object[][]{
                // token, noteId, expectedStatusCode, expectedMessage
                {null, NotesUtil.createNewNode(token), 401, "No authentication token specified in x-auth-token header"},
                {"notavalidtoken", NotesUtil.createNewNode(token), 401, "Access token is not valid or has expired, you will need to login"},
                {token, "invalidId", 400, "Note ID must be a valid ID"},
                {token, NotesUtil.createNewNode(token), 200, "Note successfully retrieved"},
        };
    }

    @Test(dataProvider = "getAllNodesTestCases")
    public void testGetAllNodes(String providedToken, int expectedStatusCode, List<String> expectedMessages) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON);

        if (providedToken != null) {
            request.header("x-auth-token", providedToken);
        }

        Matcher[] matchers = expectedMessages.stream().map(Matchers::equalTo).toArray(Matcher[]::new);

        request
                .when()
                .get("/notes")
                .then()
                .statusCode(expectedStatusCode)
                .body("message", anyOf(matchers));
    }

    @Test(dataProvider = "getNodeByIdTestCases")
    public void testGetNodeById(String providedToken, String noteId, int expectedStatusCode, String expectedMessage) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON);

        if (providedToken != null) {
            request.header("x-auth-token", providedToken);
        }

        request
                .when()
                .get("/notes/" + noteId)
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }
}
