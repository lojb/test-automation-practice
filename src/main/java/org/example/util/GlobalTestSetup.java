package org.example.util;

import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;

public abstract class GlobalTestSetup {
    protected Map<String, String> body = new HashMap<>();
    protected String token;

    @BeforeSuite
    public void setBaseURI() {
        baseURI = "https://practice.expandtesting.com/notes/api";
    }

    @BeforeClass
    public void setUp() {
        setCorrectUserBody();
        UsersUtil.registerTestUser(this.body);
        this.token = UsersUtil.getTokenForTestUser(this.body);
    }

    @BeforeMethod
    public void setCorrectUserBody() {
        this.body = UsersUtil.getCorrectBody();
    }

    @AfterClass
    public void tearDown() {
        UsersUtil.deleteAccount(body);
    }
}
