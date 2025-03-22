package com.example.bookshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Class that represents data transfer object of the Author. */
@Schema(description = "DTO model of the author")
public class AuthorDto {
    @Schema(description = "Name of the author", example = "Joshua Bloch")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}