package com.example.bookshop.controller;

import com.example.bookshop.model.Author;
import com.example.bookshop.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Class that control requests and delegate logic to other classes. **/
@RestController
@RequestMapping("/books/{bookId}/authors")
@Validated
@Tag(name = "BookShop API", description = "CRUD operations for authors")
public class AuthorController {
    private final AuthorService authorService;

    /** Constructor of the class.
     *
     * @param authorService - object of AuthorService class
     */
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /** Function to add author to the book.
     *
     * @param author object of the Author class
     * @return created author
     */
    @Operation(summary = "Create author", description = "Returns created author")
    @PostMapping
    public Author createAuthor(@Valid @RequestBody Author author, @PathVariable @Min(1) Long bookId) {
        return authorService.save(author, bookId);
    }

    /** Function to update review of the book.
     *
     * @param authorId - id of the author
     * @param author - object of the Author class
     * @return updated author
     */
    @Operation(summary = "Update existing author", description = "Update information about author")
    @PutMapping("/{authorId}")
    public Author updateAuthor(@PathVariable @Min(1) Long authorId, @Valid @RequestBody Author author) {
        return authorService.update(authorId, author);
    }

    /** Function to delete author.
     *
     * @param authorId id of the author
     */
    @Operation(summary = "Delete author", description = "Delete author by id")
    @DeleteMapping("/{authorId}")
    public void deleteAuthor(@PathVariable @Min(1) Long authorId, @PathVariable @Min(1) Long bookId) {
        authorService.delete(authorId, bookId);
    }

    /** Function to get author of the book.
     *
     * @param authorId - id of the author
     * @return author of the book
     */
    @Operation(summary = "Get author by id", description = "Return author from the specified book")
    @GetMapping("/{authorId}")
    public Author findByBookId(@PathVariable @Min(1) Long authorId, @PathVariable @Min(1) Long bookId) {
        return authorService.findById(authorId, bookId);
    }

    /** Function to get all authors from database.
     *
     * @return list of authors
     */
    @Operation(summary = "Get authors", description = "Returns all authors existing in app")
    @GetMapping("/all")
    public List<Author> findAllAuthors() {
        return authorService.findAllAuthors();
    }
}