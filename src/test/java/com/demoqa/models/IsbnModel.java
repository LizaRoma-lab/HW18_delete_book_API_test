package com.demoqa.models;

import lombok.Data;

@Data
public class IsbnModel {
    private String isbn;

    public IsbnModel(String isbn) {
        this.isbn = isbn;
    }
}
