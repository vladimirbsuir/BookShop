package com.example.bookshop.service;

import com.example.bookshop.model.Author;
import com.example.bookshop.repository.AuthorRepository;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

/** Class to store business logic of the app. */
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    /** Constructor to set authorRepository variable. */
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /** Function that returns all authors in database. */
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    /** Function that returns author with certain id. */
    public Author findById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Author not found"));
    }

    /** Function that save author in database. */
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    /** Function that updates info about author with certain id. */
    public Author update(Long id, Author author) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found");
        }
        author.setId(id);
        return authorRepository.save(author);
    }

    /** Function that deletes author with certain id. */
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
