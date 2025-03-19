package com.example.bookshop.dto;

import jakarta.validation.constraints.NotBlank;

/** Class that represents data transfer object of the Author. */
public class AuthorDto {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}