package com.demoqa.specs;

import io.restassured.specification.RequestSpecification;

import static com.demoqa.helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

public class ApiSpec {
    public static RequestSpecification requestSpec = with()
            .relaxedHTTPSValidation()
            .filter(withCustomTemplates())
            .log().uri()
            .log().method()
            .log().body()
            .contentType(JSON);
}
