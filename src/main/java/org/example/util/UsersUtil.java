package org.example.util;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UsersUtil {
    public static String baseUri = "https://practice.expandtesting.com/notes/api/users";

    public static Map<String, String> getCorrectBody() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "balazs");
        body.put("email", "balazstestuser@test.com");
        body.put("password", "balazstestpassword");
        return body;
    }

    public static void registerTestUser(Map<String, String> body) {
        ResponseBody response = given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/register")
                .getBody();

    }

    public static String getTokenForTestUser(Map<String, String> body) {
        ResponseBody response = given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/login")
                .getBody();

        return response.jsonPath().get("data.token");
    }

    public static void deleteAccount(Map<String, String> body) {
        String token = UsersUtil.getTokenForTestUser(body);
        deleteAccount(token);
    }

    public static void deleteAccount(String token) {
        if (token != null) {
            given()
                    .header("x-auth-token", token)
                    .when()
                    .delete(baseUri + "/delete-account");
        }
    }
}
