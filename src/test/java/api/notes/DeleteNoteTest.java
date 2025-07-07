package api.notes;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.util.GlobalTestSetup;
import org.example.util.NotesUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteNoteTest extends GlobalTestSetup {

    @DataProvider(name = "deleteNoteTestCases")
    public Object[][] deleteNoteTestCases() {
        return new Object[][]{
                // token, noteId, expectedStatusCode, expectedMessage
                {null, NotesUtil.createNewNode(token), 401, "No authentication token specified in x-auth-token header"},
                {"notavalidtoken", NotesUtil.createNewNode(token), 401, "Access token is not valid or has expired, you will need to login"},
                {token, "invalidId", 400, "Note ID must be a valid ID"},
                {token, NotesUtil.createNewNode(token), 200, "Note successfully deleted"},
        };
    }

    @Test(dataProvider = "deleteNoteTestCases")
    public void testDeleteNote(String providedToken, String noteId, int expectedStatusCode, String expectedMessage) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON);

        if (providedToken != null) {
            request.header("x-auth-token", providedToken);
        }

        request
                .when()
                .delete("/notes/" + noteId)
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }
}
