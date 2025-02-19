package com.example.bookshop.service;

import com.example.bookshop.model.Book;
import com.example.bookshop.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/** Class to store business logic of the app. */
@Service
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Constructor to set bookRepository variable.
     *
     * @param bookRepository объект класса BookRepository
     * */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /** Function that returns books which contains substring "title".
     *
     * @param title название книги
     * @return JSON форму объекта Book
     * */
    public List<Book> findByTitleContaining(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    /** Function that returns book with certain id.
     *
     * @param id идентификатор книги в базе данных
     * @return JSON форму объекта Book
     * */
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null); //orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    /** Function that saves book in database.
     *
     * @param book объек класса Book
     * @return JSON форму объекта Book
     * */
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    /** Function that updates info about book.
     *
     * @param id идентификатор книги
     * @param book объект класса Book
     * @return JSON форму объекта Book
     * */
    public Book update(Long id, Book book) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found");
        }
        book.setId(id);
        return bookRepository.save(book);
    }

    /**
     * Function that removes book from database.
     *
     * @param id идентификатор объекта в базе данных
     * */
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
