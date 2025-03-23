package com.example.bookshop.controller;

import com.example.bookshop.dto.ReviewDto;
import com.example.bookshop.mapper.ReviewMapper;
import com.example.bookshop.model.Review;
import com.example.bookshop.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Review requests", description = "CRUD operations for reviews")
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
    @Operation(summary = "Create review", description = "Creates new review",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Review was created"),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @PostMapping
    public Review createReview(@Parameter(description = "id of the book", example = "1", required = true)
                                   @PathVariable @Min(1) Long bookId, @Valid @RequestBody Review review) {
        return reviewService.createReview(bookId, review);
    }

    /** Function to save some reviews for one request.
     *
     * @param bookId id of the book
     * @param reviews list of the reviews
     * @return list of the reviews in JSON format
     */
    @PostMapping("/b")
    public List<Review> createReviews(@Parameter(description = "id of the book", example = "1", required = true)
                                          @PathVariable @Min(1) Long bookId, @Valid @RequestBody List<Review> reviews) {
        return reviews.stream().map(review -> reviewService.createReview(bookId, review)).toList();
    }

    /** Function to update review of the book.
     *
     * @param reviewId - id of the review
     * @param review - object of the Review class
     * @return updated review
     */
    @Operation(summary = "Update existing review", description = "Update review with specified id",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Review was updated"),
                @ApiResponse(responseCode = "404", description =
                            "Review not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Review not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @PutMapping("/{reviewId}")
    public Review updateReview(@Parameter(description = "id of the review", example = "1", required = true)
                                   @PathVariable @Min(1) Integer reviewId, @Valid @RequestBody Review review,
                                   @Parameter(description = "id of the book", example = "1", required = true)
                                   @PathVariable Long bookId) {
        return reviewService.updateReview(reviewId, review, bookId);
    }

    /** Function to delete review.
     *
     * @param reviewId - id of the review
     */
    @Operation(summary = "Delete review", description = "Delete review with specified id",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Review was deleted"),
                @ApiResponse(responseCode = "404", description =
                            "Review not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Review not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @DeleteMapping("/{reviewId}")
    public void deleteReview(@Parameter(description = "id of the review", example = "1", required = true)
                                 @PathVariable @Min(1) Integer reviewId,
                                 @Parameter(description = "id of the book", example = "1", required = true)
                                 @PathVariable @Min(1) Long bookId) {
        reviewService.deleteReview(reviewId, bookId);
    }

    /** Function to get all reviews from database.
     *
     * @return list of reviews
     */
    @Operation(summary = "Get reviews", description = "Returns all reviews existing in app",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Get all reviews"),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @GetMapping("/all")
    public List<Review> findAllReviews() {
        return reviewService.findAllReviews();
    }

    /** Function to get all reviews of the book.
     *
     * @param bookId - id of the book
     * @return reviews of the book
     */
    @Operation(summary = "Get reviews", description = "Returns all reviews from book with specified id",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Get review by id"),
                @ApiResponse(responseCode = "404", description =
                            "Review not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Review not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @GetMapping
    public List<ReviewDto> getReviewsByBookId(
            @Parameter(description = "id of the book", example = "1", required = true)
                                                  @PathVariable @Min(1) Long bookId) {
        List<Review> reviews = reviewService.getReviewsByBookId(bookId);
        return reviews.stream()
                .map(reviewMapper::toDto)
                .toList();
    }
}