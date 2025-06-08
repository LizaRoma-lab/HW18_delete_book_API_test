package com.demoqa.steps;

import com.demoqa.models.LoginBodyModel;
import com.demoqa.models.LoginResponseModel;
import io.qameta.allure.Step;

import static com.demoqa.specs.Specs.*;
import static io.restassured.RestAssured.given;

    public class AccountApiSteps {

        @Step("Аутентификация пользователя")
        public LoginResponseModel login(LoginBodyModel authData) {
            return given(requestSpec)
                    .body(authData)
                    .when()
                    .post("/Account/v1/Login")
                    .then()
                    .spec(success200Spec)
                    .extract().as(LoginResponseModel.class);
        }
}
