package com.demoqa.models;

import lombok.Data;

@Data
public class DeleteBookRequest {
    private String userId;
    private String isbn;
}
