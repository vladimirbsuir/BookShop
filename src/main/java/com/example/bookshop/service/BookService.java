package com.example.bookshop.service;

import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Class to store business logic of the app. */
@Service
public class BookService {

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
    public List<Book> findByTitleContaining(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    /** Function that returns book with certain id.
     *
     * @param id идентификатор книги в базе данных
     * @return JSON форму объекта Book
     * */
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    /** Function that saves book in database.
     *
     * @param book объек класса Book
     * @return JSON форму объекта Book
     * */
    public Book save(Book book) {
        // Проверяем авторов книги
        if (book.getAuthors() != null) {
            List<Author> managedAuthors = new ArrayList<>();
            for (Author author : book.getAuthors()) {
                // Если у автора есть ID, проверяем, существует ли он в базе
                if (author.getId() != null) {
                    Author existingAuthor = authorRepository.findById(author.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Author not found"));
                    managedAuthors.add(existingAuthor); // Используем существующего автора
                } else {
                    // Если автора с таким ID нет, сохраняем нового
                    managedAuthors.add(authorRepository.save(author));
                }
            }
            // Устанавливаем управляемых авторов для книги
            book.setAuthors(managedAuthors);
        }

        // Сохраняем книгу
        return bookRepository.save(book);
    }

    /** Function that updates info about book.
     *
     * @param id идентификатор книги
     * @param book объект класса Book
     * @return JSON форму объекта Book
     * */
    //@Transactional
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
