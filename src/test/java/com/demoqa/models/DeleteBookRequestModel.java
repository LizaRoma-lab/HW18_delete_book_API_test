package com.demoqa.models;

import lombok.Data;

@Data
public class DeleteBookRequestModel {
    private String userId;
    private String isbn;
}
