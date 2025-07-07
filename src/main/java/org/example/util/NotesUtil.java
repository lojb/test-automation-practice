package org.example.util;

import io.restassured.http.ContentType;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class NotesUtil {
    public static String baseUri = "https://practice.expandtesting.com/notes/api/notes";

    // Returns the id of the new note
    public static String createNewNode(String token) {
        Map<String, String> body = Map.of(
                "title", "testTitle",
                "description", "testDesc",
                "category", "Home"
        );


        return given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(body)
                .when()
                .post()
                .getBody()
                .jsonPath()
                .getString("data.id");
    }
}
