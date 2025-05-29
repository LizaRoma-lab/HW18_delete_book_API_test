package com.demoqa.models;

import lombok.Data;

import java.util.List;

@Data
public class AddBooksRequest {
    private String userId;
    private List<Isbn> collectionOfIsbns;

    @Data
    public static class Isbn {
        private String isbn;

        public Isbn(String isbn) {
            this.isbn = isbn;
        }

        public Isbn() {}
    }
}
