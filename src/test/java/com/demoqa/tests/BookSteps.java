package com.demoqa.tests;

import com.demoqa.models.AddBooksRequest;
import com.demoqa.models.DeleteBookRequest;
import com.demoqa.specs.ApiSpecs;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BookSteps {
    public static Response addBook(String userId, String token, String isbn) {
        AddBooksRequest request = new AddBooksRequest();
        request.setUserId(userId);
        request.setCollectionOfIsbns(List.of(new AddBooksRequest.Isbn(isbn)));

        return given()
                .spec(ApiSpecs.getAuthSpec(token))
                .body(request)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .statusCode(201)
                .extract().response();
    }
    public static void deleteBook(String userId, String token, String isbn) {
        DeleteBookRequest request = new DeleteBookRequest();
        request.setUserId(userId);
        request.setIsbn(isbn);

        given()
                .spec(ApiSpecs.getAuthSpec(token))
                .body(request)
                .when()
                .delete("/BookStore/v1/Book")
                .then()
                .statusCode(204);
    }
}
