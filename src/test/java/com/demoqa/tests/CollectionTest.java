package com.demoqa.tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.demoqa.helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;


public class CollectionTest extends TestBase {
    @Test
    void addBookToCollection_WithDeleteBook_Test() {
        // 1. Аутентификация
        Response authResponse = step("Аутентификация пользователя", () -> {
            String authData = "{\"userName\": \"test123456\", \"password\": \"Test123456@\"}";

            return given()
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
                    .extract().response();
        });

        String isbn = "9781449365035";
        String userId = authResponse.path("userId");
        String token = authResponse.path("token");

        // 2. Добавление книги в коллекцию
        step("Добавление книги в коллекцию", () -> {
            String bookData = format("{\"userId\":\"%s\",\"collectionOfIsbns\":[{\"isbn\":\"%s\"}]}",
                    userId, isbn);

            given()
                    .relaxedHTTPSValidation()
                    .filter(withCustomTemplates())
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
        });

        // 3. Удаление книги из коллекции
        step("Удаление книги из коллекции", () -> {
            String deleteBookData = format("{\"userId\":\"%s\",\"isbn\":\"%s\"}",
                    userId, isbn);

            given()
                    .relaxedHTTPSValidation()
                    .filter(withCustomTemplates())
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
        });

        // 4. Проверка в UI, что книга удалена
        step("Проверка в UI отсутствия книг", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userID", userId));
            getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
            getWebDriver().manage().addCookie(new Cookie("token", token));

            open("/profile");
            $(".rt-noData").shouldHave(text("No rows found"));
            byte[] screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Скриншот после проверки", "image/png", new ByteArrayInputStream(screenshot), "png");
        });
    }
}
