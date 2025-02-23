package com.example.bookshop.repository;

import com.example.bookshop.model.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Class that represents database containing reviews. */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    /** Function to get all reviews of the book.
     *
     * @param bookId - id of the book
     * @return reviews of the book
     */
    List<Review> findByBookId(Long bookId);
}