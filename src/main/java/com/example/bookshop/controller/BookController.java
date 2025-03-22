package com.example.bookshop.controller;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.mapper.BookMapper;
import com.example.bookshop.model.Book;
import com.example.bookshop.service.BookService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Class that control requests and delegate logic to other classes. **/
@RestController
@RequestMapping("/books")
@Validated
@Tag(name = "Book requests", description = "CRUD operations for books ")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    /** Constructor that sets bookService variable. */
    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    /**Function to get books with title containing substring.
     *
     * @param title title of the book
     * @return JSON object of Book
     * */
    @Operation(summary = "Get books", description = "Returns book with specified title",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Get book by title"),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @GetMapping
    public BookDto getBooks(@Parameter(description = "Title of the book", example = "Java")
                                @RequestParam(required = false) String title) {
        Book books = bookService.findByTitle(title);
        return bookMapper.toDto(books);
    }

    /** Function to get all books from database.
     *
     * @return JSON objects of all books
     */
    @Operation(summary = "Get books", description = "Returns all books existing in app",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Get all books"),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @GetMapping("/all")
    public List<BookDto> getAllBooks() {
        List<Book> books = bookService.findAllBooks();
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    /**Function that holds Get request and returns book with certain id.
     *
     * @param id id of the book
     * @return JSON object of Book
     * */
    @Operation(summary = "Get book", description = "Returns book with specified id",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Get book by id"),
                @ApiResponse(responseCode = "404", description =
                            "Book not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Book not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @GetMapping("/{id}")
    public BookDto getBookById(@Parameter(description = "Id of the book", example = "1", required = true)
                                   @PathVariable @Min(1) Long id) {
        Book book = bookService.findById(id);
        return bookMapper.toDto(book);
    }

    /** Function to get books with specified author.
     *
     * @param authorName name of the author
     * @return list of the books with specified author
     */
    @Operation(summary = "Get books", description = "Returns books that have specified author",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Get books by author name"),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @GetMapping("/find")
    public List<BookDto> getBooksByAuthorName(@Parameter(description = "Name of the author", example = "Joshua Bloch")
                                                  @RequestParam(required = false) String authorName) {
        return bookService.findByAuthorName(authorName).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    /** Function to get books with review amount greater than reviewCount.
     *
     * @param reviewCount amount of reviews
     * @return list of books
     */
    @Operation(summary = "Get books", description = "Returns books filtered by amount of reviews",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Get books by amount of reviews"),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @GetMapping("/find/reviews")
    public List<BookDto> getBooksByReviewCount(@Parameter(description = "Amount of reviews", example = "3")
                                                   @RequestParam(required = false) @Min(0) Long reviewCount) {
        return bookService.findByReviewCount(reviewCount).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    /** Function to create new book.
     *
     * @param book JSON object of Book
     * @return JSON object of Book
     * */
    @Operation(summary = "Create book", description = "Create new book",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Book was created"),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @PostMapping
    public Book createBook(@Valid @RequestBody Book book) {
        return bookService.save(book);
    }

    /** Function that holds Put request and updates book with certain id.
     *
     * @param id id of the book
     * @param book JSON object of Book
     * @return JSON object of Book
     * */
    @Operation(summary = "Update existing book", description = "Updates book by new title",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Book was updated"),
                @ApiResponse(responseCode = "404", description =
                            "Book not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Book not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @PutMapping("/{id}")
    public Book updateBook(@Parameter(description = "id of the book", example = "1", required = true)
                               @PathVariable @Min(1) Long id, @Valid @RequestBody Book book) {
        return bookService.update(id, book);
    }

    /** Function that holds Delete request and removes book from database.
     *
     * @param id идентификатор объекта в базе данных
     * */
    @Operation(summary = "Delete book", description = "Deletes book with specified id",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Book was deleted"),
                @ApiResponse(responseCode = "404", description =
                            "Book not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Book not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @DeleteMapping("/{id}")
    public void deleteBook(@Parameter(description = "id of the book", example = "1", required = true)
                               @PathVariable @Min(1) Long id) {
        bookService.delete(id);
    }
}