package com.demoqa.tests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;


public class CollectionTest extends TestBase {
    @Test
    void addBookToCollection_WithDeleteBook_Test() {
        // Аутентификация
        String authData = "{\"userName\": \"test123456\", \"password\": \"Test123456@\"}";

        Response authResponse = given()
                .relaxedHTTPSValidation()
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
                .extract().response();

        String isbn = "9781449365035";
        String userId = authResponse.path("userId");
        String token = authResponse.path("token");

        // Сначала добавляем книгу, чтобы она точно была в коллекции
        String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                userId, isbn);

        given()
                .relaxedHTTPSValidation()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + token)
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(201);

        // Теперь удаляем книгу
        String deleteBookData = format("{\"userId\":\"%s\",\"isbn\":\"%s\"}",
                userId, isbn);

        given()
                .relaxedHTTPSValidation()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + token)
                .body(deleteBookData)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);

        // Проверяем в UI
        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", userId));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", token));

        open("/profile");
        $(".rt-noData").shouldHave(text("No rows found"));
    }
}
