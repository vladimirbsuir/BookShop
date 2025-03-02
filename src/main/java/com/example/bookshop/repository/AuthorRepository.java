package com.example.bookshop.repository;

import com.example.bookshop.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Class that represents database containing authors. **/
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    /** Function to find author bi name.
     *
     * @param name - name of the author
     * @return object of class Author
     */
    Author findByName(String name);

    /** Function to check existence of object in database by name.
     *
     * @param name name pf the author
     * @return true if author exists, false otherwise
     */
    boolean existsByName(String name);
}