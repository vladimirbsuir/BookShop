package com.example.bookshop.mapper;

import static org.junit.jupiter.api.Assertions.*;
import com.example.bookshop.dto.ReviewDto;
import com.example.bookshop.model.Review;
import org.junit.jupiter.api.Test;

class ReviewMapperTest {

    private final ReviewMapper reviewMapper = new ReviewMapper();

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        Review review = new Review();
        review.setMessage("Excellent book!");

        ReviewDto dto = reviewMapper.toDto(review);

        assertEquals(review.getMessage(), dto.getMessage());
    }
}