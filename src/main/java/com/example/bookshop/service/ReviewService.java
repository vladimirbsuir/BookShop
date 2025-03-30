package com.example.bookshop.service;

import com.example.bookshop.exception.ResourceNotFoundException;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.repository.ReviewRepository;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/** Class that make CRUD operations with Review object. */
@Service
public class ReviewService {

    private static final String ERROR_MESSAGE = "Book not found";

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    /** Constructor of the class.
     *
     * @param reviewRepository object of the ReviewRepository class
     */
    public ReviewService(ReviewRepository reviewRepository,
                         BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    /** Function to add review to the book.
     *
     * @param bookId id of the book
     * @param review object of the Review class
     * @return created review
     */
    @CacheEvict(value = {"reviews", "books"}, key = "#bookId")
    public Review createReview(Long bookId, Review review) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, ERROR_MESSAGE));

        review.setBook(book);
        return reviewRepository.save(review);
    }

    /** Function to update review of the book.
     *
     * @param reviewId id of the review
     * @param review object of the Review class
     * @param bookId id of the book
     * @return updated review
     */
    @CacheEvict(value = {"reviews", "books"}, key = "#bookId")
    public Review updateReview(Integer reviewId, Review review, Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, ERROR_MESSAGE);
        }

        Review initialReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Review not found"));
        initialReview.setMessage(review.getMessage());
        return reviewRepository.save(initialReview);
    }

    /** Function to delete review.
     *
     * @param reviewId id of the review
     */
    @CacheEvict(value = {"reviews", "books"}, key = "#bookId")
    public void deleteReview(Integer reviewId, Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, ERROR_MESSAGE);
        }
        reviewRepository.deleteById(reviewId);
    }

    /** Function to get all reviews of the book.
     *
     * @param bookId id of the book
     * @return reviews of the book
     */
    @Cacheable("reviews")
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