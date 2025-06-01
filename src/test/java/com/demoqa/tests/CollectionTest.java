package com.demoqa.tests;

import com.demoqa.models.*;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.util.List;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.demoqa.helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class CollectionTest extends TestBase {
    @Test
    void addBookToCollection_WithDeleteBook_Test() {
        // 1. Аутентификация
        LoginBodyModel authData = new LoginBodyModel();
        authData.setUserName("test123456");
        authData.setPassword("Test123456@");

        LoginResponseModel authResponse = step("Аутентификация пользователя", () ->
                given()
                        .relaxedHTTPSValidation()
                        .filter(withCustomTemplates())
                        .log().uri()
                        .log().method()
                        .log().body()
                        .contentType(JSON)
                        .body(authData)
                        .when()
                        .post("/Account/v1/Login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseModel.class)
        );

        String isbn = "9781449365035";
        String userId = authResponse.getUserId();
        String token = authResponse.getToken();

        // 2. Добавление книги в коллекцию
        AddBookRequestModel bookRequest = new AddBookRequestModel();
        bookRequest.setUserId(userId);
        bookRequest.setCollectionOfIsbns(List.of(new IsbnModel(isbn)));
        step("Добавление книги в коллекцию", () -> {
            given()
                    .relaxedHTTPSValidation()
                    .filter(withCustomTemplates())
                    .log().uri()
                    .log().method()
                    .log().body()
                    .contentType(JSON)
                    .header("Authorization", "Bearer " + token)
                    .body(bookRequest)
                    .when()
                    .post("/BookStore/v1/Books")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(201);
        });

        // 3. Удаление книги из коллекции
        DeleteBookRequestModel deleteBookRequest = new DeleteBookRequestModel();
        deleteBookRequest.setUserId(userId);
        deleteBookRequest.setIsbn(isbn);
        step("Удаление книги из коллекции", () -> {
            given()
                    .relaxedHTTPSValidation()
                    .filter(withCustomTemplates())
                    .log().uri()
                    .log().method()
                    .log().body()
                    .contentType(JSON)
                    .header("Authorization", "Bearer " + token)
                    .body(deleteBookRequest)
                    .when()
                    .delete("/BookStore/v1/Book")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(204);
        });

        // 4. Проверка в UI, что книга удалена
        step("Проверка в UI отсутствия книг", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userID", userId));
            getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
            getWebDriver().manage().addCookie(new Cookie("token", token));

            open("/profile");
            $(".rt-noData").shouldHave(text("No rows found"));
            byte[] screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Скриншот после проверки", "image/png", new ByteArrayInputStream(screenshot), "png");
        });
    }
}

