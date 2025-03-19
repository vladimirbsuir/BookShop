package com.example.bookshop.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/** Class that represents data transfer object of the Book. */
public class BookDto {
    private String title;
    private List<AuthorDto> authors;
    private List<ReviewDto> reviews;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AuthorDto> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDto> authors) {
        this.authors = authors;
    }

    public List<ReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDto> reviews) {
        this.reviews = reviews;
    }
}