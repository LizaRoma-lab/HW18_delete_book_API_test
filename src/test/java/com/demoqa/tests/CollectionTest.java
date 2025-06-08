package com.demoqa.tests;

import com.demoqa.models.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.demoqa.specs.Specs.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class CollectionTest extends TestBase {
    @Test
    void addBookToCollection_WithDeleteBook_Test() {
        // 1. Аутентификация
        LoginBodyModel authData = new LoginBodyModel();
        authData.setUserName("test123456");
        authData.setPassword("Test123456@");

        LoginResponseModel authResponse = step("Аутентификация пользователя", () ->
                given(requestSpec)
                        .body(authData)
                        .when()
                        .post("/Account/v1/Login")
                        .then()
                        .spec(success200Spec)
                        .extract().as(LoginResponseModel.class)
        );

        String isbn = "9781449365035";
        String userId = authResponse.getUserId();
        String token = authResponse.getToken();

        // 2. Очистка коллекции книг
        step("Очистка коллекции книг", () -> {
            given(requestSpec)
                    .header("Authorization", "Bearer " + token)
                    .queryParam("UserId", userId)
                    .when()
                    .delete("/BookStore/v1/Books")
                    .then()
                    .spec(success204Spec);
        });

        // 3. Добавление книги в коллекцию
        AddBookRequestModel bookRequest = new AddBookRequestModel();
        bookRequest.setUserId(userId);
        bookRequest.setCollectionOfIsbns(List.of(new IsbnModel(isbn)));
        step("Добавление книги в коллекцию", () -> {
            given(requestSpec)
                    .header("Authorization", "Bearer " + token)
                    .body(bookRequest)
                    .when()
                    .post("/BookStore/v1/Books")
                    .then()
                    .spec(success201Spec);
        });

        // 4. Удаление книги из коллекции
        DeleteBookRequestModel deleteBookRequest = new DeleteBookRequestModel();
        deleteBookRequest.setUserId(userId);
        deleteBookRequest.setIsbn(isbn);
        step("Удаление книги из коллекции", () -> {
            given(requestSpec)
                    .header("Authorization", "Bearer " + token)
                    .body(deleteBookRequest)
                    .when()
                    .delete("/BookStore/v1/Book")
                    .then()
                    .spec(success204Spec);
        });

        // 5. Проверка в UI, что книга удалена
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

