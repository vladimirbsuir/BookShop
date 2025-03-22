package com.example.bookshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/** Class that represents data transfer object of the Book. */
@Schema(description = "DTO model of the book")
public class BookDto {
    @Schema(description = "Title of the book")
    private String title;
    @Schema(description = "List of the authors of the book")
    private List<AuthorDto> authors;
    @Schema(description = "List of the reviews of the book")
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