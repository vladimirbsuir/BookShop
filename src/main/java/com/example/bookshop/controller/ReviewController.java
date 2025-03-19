package com.example.bookshop.controller;

import com.example.bookshop.dto.ReviewDto;
import com.example.bookshop.mapper.ReviewMapper;
import com.example.bookshop.model.Review;
import com.example.bookshop.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.validation.annotation.Validated;
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
@Validated
@Tag(name = "BookShop API", description = "CRUD operations for reviews")
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
    @Operation(summary = "Create review", description = "Creates new review")
    @PostMapping
    public Review createReview(@PathVariable @Min(1) Long bookId, @Valid @RequestBody Review review) {
        return reviewService.createReview(bookId, review);
    }

    /** Function to update review of the book.
     *
     * @param reviewId - id of the review
     * @param review - object of the Review class
     * @return updated review
     */
    @Operation(summary = "Update existing review", description = "Update review with specified id")
    @PutMapping("/{reviewId}")
    public Review updateReview(@PathVariable @Min(1) Integer reviewId, @Valid @RequestBody Review review, @PathVariable Long bookId) {
        return reviewService.updateReview(reviewId, review, bookId);
    }

    /** Function to delete review.
     *
     * @param reviewId - id of the review
     */
    @Operation(summary = "Delete review", description = "Delete review with specified id")
    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable @Min(1) Integer reviewId, @PathVariable @Min(1) Long bookId) {
        reviewService.deleteReview(reviewId, bookId);
    }

    /** Function to get all reviews from database.
     *
     * @return list of reviews
     */
    @Operation(summary = "Get reviews", description = "Returns all reviews existing in app")
    @GetMapping("/all")
    public List<Review> findAllReviews() {
        return reviewService.findAllReviews();
    }

    /** Function to get all reviews of the book.
     *
     * @param bookId - id of the book
     * @return reviews of the book
     */
    @Operation(summary = "Get reviews", description = "Returns all reviews from book with specified id")
    @GetMapping
    public List<ReviewDto> getReviewsByBookId(@PathVariable @Min(1) Long bookId) {
        List<Review> reviews = reviewService.getReviewsByBookId(bookId);
        return reviews.stream()
                .map(reviewMapper::toDto)
                .toList();
    }
}