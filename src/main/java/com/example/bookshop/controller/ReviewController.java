package com.example.bookshop.controller;

import com.example.bookshop.dto.ReviewDto;
import com.example.bookshop.mapper.ReviewMapper;
import com.example.bookshop.model.Review;
import com.example.bookshop.service.ReviewService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Class to hold CRUD operations with reviews. */
@RestController
@RequestMapping("/books/{bookId}/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    /** Constructor of the class.
     *
     * @param reviewService - object of ReviewService class
     */
    public ReviewController(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    /** Function to add review to the book.
     *
     * @param bookId - id of the book
     * @param review - object of the Review class
     * @return created review
     */
    @PostMapping
    public Review createReview(@PathVariable Long bookId, @RequestBody Review review) {
        return reviewService.createReview(bookId, review);
    }

    /** Function to update review of the book.
     *
     * @param reviewId - id of the review
     * @param review - object of the Review class
     * @return updated review
     */
    @PutMapping("/{reviewId}")
    public Review updateReview(@PathVariable Integer reviewId, @RequestBody Review review) {
        return reviewService.updateReview(reviewId, review);
    }

    /** Function to delete review.
     *
     * @param reviewId - id of the review
     */
    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
    }

    /** Function to get all reviews from database.
     *
     * @return list of reviews
     */
    @GetMapping("/all")
    public List<Review> findAllReviews() {
        return reviewService.findAllReviews();
    }

    /** Function to get all reviews of the book.
     *
     * @param bookId - id of the book
     * @return reviews of the book
     */
    @GetMapping
    public List<ReviewDto> getReviewsByBookId(@PathVariable Long bookId) {
        List<Review> reviews = reviewService.getReviewsByBookId(bookId);
        return reviews.stream()
                .map(reviewMapper::toDto)
                .toList();
    }
}