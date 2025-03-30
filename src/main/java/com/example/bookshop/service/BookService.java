package com.example.bookshop.service;

import com.example.bookshop.exception.ResourceNotFoundException;
import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/** Class to store business logic of the app. */
@Service
public class BookService {

    private static final String BOOK_NOT_FOUND_MESSAGE = "Book not found";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /**
     * Constructor to set bookRepository variable.
     *
     * @param bookRepository объект класса BookRepository
     * */
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    /** Function that returns books which contains substring "title".
     *
     * @param title название книги
     * @return JSON форму объекта Book
     * */
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    /** Function to get all books from database.
     *
     * @return all books in database
     */
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    /** Function that returns book with certain id.
     *
     * @param id идентификатор книги в базе данных
     * @return JSON форму объекта Book
     * */
    @Cacheable("books")
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_MESSAGE));
    }

    /** Function that returns books with specified author.
     *
     * @param authorName name of the author
     * @return list of books with specified author
     */
    public List<Book> findByAuthorName(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    /** Function to get books with amount of reviews greater than reviewCount.
     *
     * @param reviewCount amount of reviews
     * @return list of books
     */
    public List<Book> findByReviewCount(Long reviewCount) {
        return bookRepository.findByReviewCount(reviewCount);
    }

    /** Function that saves book in database.
     *
     * @param book объек класса Book
     * @return JSON форму объекта Book
     * */
    public Book save(Book book) {
        if (book.getAuthors() != null) {
            List<Author> savedAuthors = new ArrayList<>();

            for (Author author : book.getAuthors()) {

                if (authorRepository.existsByName(author.getName())) {
                    Author existingAuthor = authorRepository.findByName(author.getName());
                    savedAuthors.add(existingAuthor);
                } else {
                    savedAuthors.add(authorRepository.save(author));
                }
            }

            book.setAuthors(savedAuthors);
        }

        if (book.getReviews() != null) {
            for (Review review : book.getReviews()) {
                review.setBook(book);
            }
        }

        return bookRepository.save(book);
    }

    /** Function that updates info about book.
     *
     * @param id идентификатор книги
     * @param book объект класса Book
     * @return JSON форму объекта Book
     * */
    @CachePut(value = "books", key = "#id")
    public Book update(Long id, Book book) {
        Book existsBook = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_MESSAGE));
        book.setAuthors(existsBook.getAuthors());
        book.setReviews(existsBook.getReviews());

        book.setId(id);
        return bookRepository.save(book);
    }

    /**
     * Function that removes book from database.
     *
     * @param id идентификатор объекта в базе данных
     * */
    @CacheEvict(value = {"books", "authors", "reviews"}, allEntries = true)
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
