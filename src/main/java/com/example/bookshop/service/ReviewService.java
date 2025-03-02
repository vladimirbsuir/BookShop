package com.example.bookshop.service;

import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/** Class that make CRUD operations with Review object. */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookService bookService;

    /** Constructor of the class.
     *
     * @param reviewRepository object of the ReviewRepository class
     * @param bookService object of the BookRepository class
     */
    public ReviewService(ReviewRepository reviewRepository, BookService bookService) {
        this.reviewRepository = reviewRepository;
        this.bookService = bookService;
    }

    /** Function to add review to the book.
     *
     * @param bookId id of the book
     * @param review object of the Review class
     * @return created review
     */
    public Review createReview(Long bookId, Review review) {
        Book book = bookService.findById(bookId);
        if (book == null) {
            throw new EntityNotFoundException("Book not found");
        }

        review.setBook(book);
        return reviewRepository.save(review);
    }

    /** Function to update review of the book.
     *
     * @param id id of the review
     * @param review object of the Review class
     * @return updated review
     */
    public Review updateReview(Integer id, Review review) {
        if (!reviewRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found");
        }
        Review initialReview = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        initialReview.setMessage(review.getMessage());
        return reviewRepository.save(initialReview);
    }

    /** Function to delete review.
     *
     * @param reviewId id of the review
     */
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    /** Function to get all reviews of the book.
     *
     * @param bookId id of the book
     * @return reviews of the book
     */
    public List<Review> getReviewsByBookId(Long bookId) {

        return reviewRepository.findByBookId(bookId);
    }

    /** Function to get all reviews from database.
     *
     * @return list of reviews
     */
    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }
}