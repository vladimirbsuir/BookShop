package com.example.bookshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Class that represents data transfer object of the Review. */
@Schema(description = "DTO model of the review")
public class ReviewDto {
    @Schema(description = "Text of the review")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
