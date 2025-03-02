package com.example.bookshop.controller;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.mapper.BookMapper;
import com.example.bookshop.model.Book;
import com.example.bookshop.service.BookService;
import java.util.List;
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
public class BookController {

    /** Variable to save BookService object. */
    private final BookService bookService;

    private final BookMapper bookMapper;

    /** Constructor that sets bookService variable. */
    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    /**
     * Function to get books with title containing substring.
     *
     * @param title название книги
     * @return JSON форму объекта Book
     * */
    @GetMapping
    public List<BookDto> getBooks(@RequestParam(required = false) String title) {
        List<Book> books = bookService.findByTitleContaining(title);
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    /** Function to get all books from database.
     *
     * @return JSON objects of all books
     */
    @GetMapping("/all")
    public List<BookDto> getAllBooks() {
        List<Book> books = bookService.findAllBooks();
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    /**
     * Function that holds Get request and returns book with certain id.
     *
     * @param id идентификатор объекта в базе данных
     * @return JSON форму объекта Book
     * */
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return bookMapper.toDto(book);
    }

    /**
     * Function that holds Post request and save book in database.
     *
     * @param book JSON форма объекта
     * @return JSON форму объекта Book
     * */
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.save(book);
    }

    /**
     * Function that holds Put request and updates book with certain id.
     *
     * @param id идентификатор объекта в базе данных
     * @param book JSON форма объекта
     * @return JSON форму объекта Book
     * */
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    /** Function that holds Delete request and removes book from database.
     *
     * @param id идентификатор объекта в базе данных
     * */
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}