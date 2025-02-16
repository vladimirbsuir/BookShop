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

    /** Constructor to set bookRepository variable. */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /** Function that returns books which contains substring "title". */
    public List<Book> findByTitleContaining(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    /** Function that returns book with certain id. */
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    /** Function that saves book in database. */
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    /** Function that updates info about book. */
    public Book update(Long id, Book book) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found");
        }
        book.setId(id);
        return bookRepository.save(book);
    }

    /** Function that removes book from database. */
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
