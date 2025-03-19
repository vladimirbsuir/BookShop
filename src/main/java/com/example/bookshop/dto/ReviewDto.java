package com.example.bookshop.dto;

import jakarta.validation.constraints.NotBlank;

/** Class that represents data transfer object of the Review. */
public class ReviewDto {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
