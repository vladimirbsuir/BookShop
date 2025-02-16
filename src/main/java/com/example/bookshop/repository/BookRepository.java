package com.example.bookshop.repository;

import com.example.bookshop.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Class that represents database containing books. */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /** Function that returns books containing substring "title". */
    List<Book> findByTitleContaining(String title);
}