package com.demoqa.steps;

import com.demoqa.models.AddBookRequestModel;
import com.demoqa.models.DeleteBookRequestModel;
import io.qameta.allure.Step;

import static com.demoqa.specs.Specs.*;
import static io.restassured.RestAssured.given;

public class BookStoreApiSteps {

    @Step("Очистка коллекции книг")
    public void cleanBooks(String userId, String token) {
        given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .queryParam("UserId", userId)
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .spec(success204Spec);
    }

    @Step("Добавление книги в коллекцию")
    public void addBook(AddBookRequestModel bookRequest, String token) {
        given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(bookRequest)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(success201Spec);
    }

    @Step("Удаление книги из коллекции")
    public void deleteBook(DeleteBookRequestModel deleteBookRequest, String token) {
        given(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(deleteBookRequest)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .spec(success204Spec);
    }
}
