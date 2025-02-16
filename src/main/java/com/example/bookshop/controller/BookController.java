package com.example.bookshop.controller;

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

    /** Constructor that sets bookService variable. */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /** Function to get books with title containing substring. */
    @GetMapping
    public List<Book> getBooks(@RequestParam String title) {
        return bookService.findByTitleContaining(title);
    }

    /** Function that holds Get request and returns book with certain id. */
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    /** Function that holds Post request and save book in database. */
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.save(book);
    }

    /** Function that holds Put request and updates book with certain id. */
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    /** Function that holds Delete request and removes book from database. */
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
