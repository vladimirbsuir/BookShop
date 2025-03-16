package com.example.bookshop.repository;

import com.example.bookshop.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Class that represents database containing books. */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /** Function that returns book with concrete title.
     *
     * @param title название книги
     * @return JSON форму объекта Book
     * */
    Book findByTitle(String title);

    /** Function to find book by id.
     *
     * @param id id of the book
     * @return object of Book class
     */
    @EntityGraph(value = "Book", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Book> findById(Long id);

    /** Function with custom query to get books by author name.
     *
     * @param authorName name of the author
     * @return list of books with specified author
     */
    @Query("SELECT book FROM Book book JOIN book.authors author WHERE author.name = :authorName")
    List<Book> findByAuthorName(@Param("authorName") String authorName);
}