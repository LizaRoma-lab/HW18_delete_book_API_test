package com.demoqa.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.demoqa.helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;

public class Specs {
    public static RequestSpecification requestSpec = with()
            .relaxedHTTPSValidation()
            .filter(withCustomTemplates())
            .log().uri()
            .log().method()
            .log().body()
            .contentType(JSON);

    public static ResponseSpecification successResponseSpec(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(STATUS)
                .log(BODY)
                .build();
    }

    public static ResponseSpecification success200Spec = successResponseSpec(200);
    public static ResponseSpecification success201Spec = successResponseSpec(201);
    public static ResponseSpecification success204Spec = successResponseSpec(204);
}

