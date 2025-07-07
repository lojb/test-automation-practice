package api.users;

import io.restassured.http.ContentType;
import org.example.util.GlobalTestSetup;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RegisterAPITest extends GlobalTestSetup {

    // Override setup method from super so the test user is not registered before the class
    @BeforeClass
    @Override
    public void setUp() {
        setCorrectUserBody();
    }

    @DataProvider(name = "registerTestCases")
    public Object[][] registerTestCases() {
        Map<String, String> validBody = new HashMap<>(body);
        Map<String, String> invalidUserNameBody = new HashMap<>(body);
        Map<String, String> invalidPasswordBody = new HashMap<>(body);
        Map<String, String> invalidEmailBody = new HashMap<>(body);

        invalidUserNameBody.put("name", "asd");
        invalidPasswordBody.put("password", "asd");
        invalidEmailBody.put("email", "testemail");

        return new Object[][]{
                // requestBody, expectedStatusCode, expectedMessage
                { invalidUserNameBody, 400, "User name must be between 4 and 30 characters" },
                { invalidEmailBody, 400, "A valid email address is required" },
                { invalidPasswordBody, 400, "Password must be between 6 and 30 characters" },
                { validBody, 201, "User account created successfully" }
        };
    }

    @Test(dataProvider = "registerTestCases")
    public void testRegisterUser(Map<String, Object> requestBody, int expectedStatusCode, String expectedMessage) {
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/users/register")
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }
}
