package com.demoqa.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static com.demoqa.helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;


public class LoginSpec {
    public static RequestSpecification loginRequestSpec = with()
                .relaxedHTTPSValidation()
                .filter(withCustomTemplates())
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON);
    }


