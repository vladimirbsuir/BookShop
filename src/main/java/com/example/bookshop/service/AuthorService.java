package com.example.bookshop.service;

import com.example.bookshop.exception.ResourceNotFoundException;
import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/** Class to store business logic of the app. */
@Service
public class AuthorService {

    private static final String ERROR_MESSAGE = "Author not found";
    private static final String BOOK_NOT_FOUND_MESSAGE = "Book not found";

    private final AuthorRepository authorRepository;
    private final BookService bookService;
    private final BookRepository bookRepository;

    /** Constructor to set authorRepository variable. */
    public AuthorService(AuthorRepository authorRepository, BookService bookService, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    /** Function that returns author with certain id.
     *
     * @param id id of the author
     * @param bookId id of the book
     * @return JSON форму объекта Author
     * */
    @Cacheable(value = "authors", key = "#id")
    public Author findById(Long id, Long bookId) {

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_MESSAGE));
        List<Author> authors = book.getAuthors();
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, ERROR_MESSAGE));
        if (authors.contains(author)) {
            return author;
        } else {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, ERROR_MESSAGE);
        }
    }

    /** Function to get all authors from database.
     *
     * @return list pf authors
     */
    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    /** Function that save author in database.
     *
     * @param author объект класса Author
     * @return JSON форму объекта Author
     * */
    @CacheEvict(value = "books", allEntries = true)
    public Author save(Author author, Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_MESSAGE));

        List<Book> newBooks = new ArrayList<>();

        if (authorRepository.existsByName(author.getName())) {
            author = authorRepository.findByName(author.getName());
            newBooks = author.getBooks();
        }

        List<Author> authors = book.getAuthors();
        if (!authors.contains(author)) {
            authors.add(author);
            book.setAuthors(authors);
            newBooks.add(book);
            author.setBooks(newBooks);
        }

        return authorRepository.save(author);
    }

    /** Function that updates info about author with certain id.
     *
     * @param id идентификатор объекта в базе данных
     * @param author объект класса Author
     * @return JSON форму объекта Author
     * */
    @Caching(evict = {
        @CacheEvict(value = "authors", key = "#id"),
        @CacheEvict(value = "books", allEntries = true)
    })
    public Author update(Long id, Author author) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, ERROR_MESSAGE);
        }

        author.setId(id);
        return authorRepository.save(author);
    }

    /** Function that deletes author with certain id. */
    @Caching(evict = {
        @CacheEvict(value = "authors", key = "#id"),
        @CacheEvict(value = "books", allEntries = true)
    })
    public void delete(Long id, Long bookId) {

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, BOOK_NOT_FOUND_MESSAGE));
        List<Author> authors = book.getAuthors();
        Author author = authorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, ERROR_MESSAGE));

        authors.remove(author);

        book.setAuthors(authors);
        bookService.update(bookId, book);

        List<Book> books = author.getBooks();
        books.remove(book);
        if (books.isEmpty()) {
            authorRepository.delete(author);
        } else {
            author.setBooks(books);
            update(id, author);
        }
    }
}