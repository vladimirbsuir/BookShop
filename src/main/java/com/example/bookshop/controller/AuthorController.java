package com.example.bookshop.controller;

import com.example.bookshop.model.Author;
import com.example.bookshop.service.AuthorService;
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
    @PostMapping
    public Author createAuthor(@RequestBody Author author, @PathVariable Long bookId) {
        return authorService.save(author, bookId);
    }

    /** Function to update review of the book.
     *
     * @param authorId - id of the author
     * @param author - object of the Author class
     * @return updated author
     */
    @PutMapping("/{authorId}")
    public Author updateAuthor(@PathVariable Long authorId, @RequestBody Author author) {
        return authorService.update(authorId, author);
    }

    /** Function to delete author.
     *
     * @param authorId id of the author
     */
    @DeleteMapping("/{authorId}")
    public void deleteReview(@PathVariable Long authorId) {
        authorService.delete(authorId);
    }

    /** Function to get author of the book.
     *
     * @param authorId - id of the author
     * @return author of the book
     */
    @GetMapping("/{authorId}")
    public Author findByBookId(@PathVariable Long authorId) {
        return authorService.findById(authorId);
    }
}