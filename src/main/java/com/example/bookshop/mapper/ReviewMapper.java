package com.example.bookshop.mapper;

import com.example.bookshop.dto.ReviewDto;
import com.example.bookshop.model.Review;
import org.springframework.stereotype.Component;

/** Class to transform object from dto and vice versa. */
@Component
public class ReviewMapper {

    /** Function to transform standard object to DTO.
     *
     * @param review object of Review class
     * @return DTO object
     */
    public ReviewDto toDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setMessage(review.getMessage());
        return reviewDto;
    }

    /** Function to transform DTO to standard object.
     *
     * @param reviewDto object of ReviewDto class
     * @return standard Review object
     */
    public Review toEntity(ReviewDto reviewDto) {
        Review review = new Review();
        review.setMessage(reviewDto.getMessage());
        return review;
    }
}
