package com.example.bookshop.controller;

import com.example.bookshop.model.Author;
import com.example.bookshop.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Author requests", description = "CRUD operations for authors")
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
    @Operation(summary = "Create author", description = "Creates and returns created author",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Book was created"),
                @ApiResponse(responseCode = "400", description =
                            "Invalid request",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Invalid request\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))
            })
    @PostMapping
    public Author createAuthor(@Valid @RequestBody Author author,
                               @Parameter(description = "id of the book", example = "1", required = true)
                               @PathVariable @Min(1) Long bookId) {
        return authorService.save(author, bookId);
    }

    /** Function to save some authors for one request.
     *
     * @param authors list of the authors
     * @param bookId id of the book
     * @return list of the authors in JSON format
     */
    @PostMapping("/b")
    public List<Author> createAuthors(@Valid @RequestBody List<Author> authors,
                                      @Parameter(description = "id of the book", example = "1", required = true)
                                      @PathVariable @Min(1) Long bookId) {
        return authors.stream().map(author -> authorService.save(author, bookId)).toList();
    }

    /** Function to update author of the book.
     *
     * @param authorId - id of the author
     * @param author - object of the Author class
     * @return updated author
     */
    @Operation(summary = "Update existing author", description = "Update information about author",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Author was updated"),
                @ApiResponse(responseCode = "404", description =
                            "Author not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Author not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @PutMapping("/{authorId}")
    public Author updateAuthor(@Parameter(description = "id of the author", example = "1", required = true)
                                   @PathVariable @Min(1) Long authorId, @Valid @RequestBody Author author) {
        return authorService.update(authorId, author);
    }

    /** Function to delete author.
     *
     * @param authorId id of the author
     */
    @Operation(summary = "Delete author", description = "Delete author by id",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Author was deleted"),
                @ApiResponse(responseCode = "404", description =
                            "Author not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Author not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @DeleteMapping("/{authorId}")
    public void deleteAuthor(@Parameter(description = "id of the author", example = "1", required = true)
                                 @PathVariable @Min(1) Long authorId,
                                 @Parameter(description = "id of the book", example = "1", required = true)
                                 @PathVariable @Min(1) Long bookId) {
        authorService.delete(authorId, bookId);
    }

    /** Function to get author of the book.
     *
     * @param authorId - id of the author
     * @return author of the book
     */
    @Operation(summary = "Get author by id", description = "Return author from the specified book",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Get author by id"),
                @ApiResponse(responseCode = "404", description =
                            "Author not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Author not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @GetMapping("/{authorId}")
    public Author findByBookId(@Parameter(description = "id of the author", example = "1", required = true)
                                   @PathVariable @Min(1) Long authorId,
                                   @Parameter(description = "id of the book", example = "1", required = true)
                                   @PathVariable @Min(1) Long bookId) {
        return authorService.findById(authorId, bookId);
    }

    /** Function to get all authors from database.
     *
     * @return list of authors
     */
    @Operation(summary = "Get authors", description = "Returns all authors existing in app", responses = {
        @ApiResponse(responseCode = "200", description =
                    "Get all authors"),
        @ApiResponse(responseCode = "500", description =
                    "Internal server error",
                    content = @Content(schema = @Schema(example =
                            "{ \"error\": \"Internal server error\" }")))})
    @GetMapping("/all")
    public List<Author> findAllAuthors() {
        return authorService.findAllAuthors();
    }
}