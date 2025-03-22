package com.example.bookshop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

/** Class to hold info about authors. **/
@Entity
@Schema(description = "Model of the author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifier of the author", example = "1")
    private Long id;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"authors", "reviews"})
    @Schema(description = "List of the books of the author")
    private List<Book> books;

    @NotBlank(message = "Name shouldn't be empty")
    @Size(max = 100, message = "Max 100 characters for name")
    @Pattern(regexp = "^[a-zA-Z'\\s]+$", message = "Name must contain only letters, apostrophes, and spaces")
    @Schema(description = "Name of the author", example = "Joshua Bloch")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}