package com.demoqa.tests;

import com.demoqa.models.AuthRequest;
import com.demoqa.specs.ApiSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthSteps {
    public static Response login(String username, String password) {
        AuthRequest authData = new AuthRequest();
        authData.setUserName(username);
        authData.setPassword(password);

        return given()
                .spec(ApiSpecs.getBaseSpec())
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .statusCode(200)
                .extract().response();
    }
}
