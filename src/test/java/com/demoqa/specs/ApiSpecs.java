package com.demoqa.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public class ApiSpecs {
    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://demoqa.com")
                .setContentType(JSON)
                .setRelaxedHTTPSValidation()
                .build();
    }

    public static RequestSpecification getAuthSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpec())
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }
}
