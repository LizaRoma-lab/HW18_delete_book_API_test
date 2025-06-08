package com.demoqa.tests;

import com.demoqa.models.*;
import com.demoqa.steps.AccountApiSteps;
import com.demoqa.steps.BookStoreApiSteps;
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
import static io.qameta.allure.Allure.step;

public class CollectionTest extends TestBase {
    private final AccountApiSteps accountApi = new AccountApiSteps();
    private final BookStoreApiSteps bookStoreApi = new BookStoreApiSteps();

    @Test
    void addBookToCollection_WithDeleteBook_Test() {
        // 1. Аутентификация
        LoginBodyModel authData = new LoginBodyModel("test123456", "Test123456@");
        LoginResponseModel authResponse = accountApi.login(authData);

        String isbn = "9781449365035";
        String userId = authResponse.getUserId();
        String token = authResponse.getToken();

        // 2. Очистка коллекции книг
        bookStoreApi.cleanBooks(userId, token);

        // 3. Добавление книги в коллекцию
        AddBookRequestModel bookRequest = new AddBookRequestModel();
        bookRequest.setUserId(userId);
        bookRequest.setCollectionOfIsbns(List.of(new IsbnModel(isbn)));
        bookStoreApi.addBook(bookRequest, token);

        // 4. Удаление книги из коллекции
        DeleteBookRequestModel deleteBookRequest = new DeleteBookRequestModel();
        deleteBookRequest.setUserId(userId);
        deleteBookRequest.setIsbn(isbn);
        bookStoreApi.deleteBook(deleteBookRequest, token);

        // 5. Проверка в UI, что книга удалена
        step("Проверка в UI отсутствия книг", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userID", userId));
            getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
            getWebDriver().manage().addCookie(new Cookie("token", token));

            open("/profile");
            $(".rt-noData").shouldHave(text("No rows found"));
            byte[] screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Скриншот после проверки", "image/png",
                    new ByteArrayInputStream(screenshot), "png");
        });
    }
}
