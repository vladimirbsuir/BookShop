package com.example.bookshop.controller;

import com.example.bookshop.mapper.ReviewMapper;
import com.example.bookshop.model.Review;
import com.example.bookshop.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Configuration
    static class TestConfiguration {
        @Bean
        public ReviewController reviewController() {
            return new ReviewController(reviewService(), reviewMapper());
        }

        @Bean
        public ReviewService reviewService() {
            return mock(ReviewService.class);
        }

        @Bean
        public ReviewMapper reviewMapper() {
            return mock(ReviewMapper.class);
        }
    }

    @Test
    void createReview_ValidRequest_ReturnsReview() throws Exception {
        Review review = new Review();
        review.setMessage("Good book!");

        when(reviewService.createReview(eq(1L), any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/books/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk());
    }

    @Test
    void getReviewsByBookId_ValidRequest_ReturnsReviews() throws Exception {
        Review review1 = new Review();
        review1.setMessage("Good book!");
        Review review2 = new Review();
        review2.setMessage("Nice to read");
        List<Review> reviews = Arrays.asList(review1, review2);

        when(reviewService.getReviewsByBookId(1L)).thenReturn(reviews);

        mockMvc.perform(get("/books/1/reviews"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllReviews_ValidRequest_ReturnsReviews() throws Exception {
        Review review1 = new Review();
        review1.setMessage("Good book!");
        Review review2 = new Review();
        review2.setMessage("Interesting book!");
        List<Review> reviews = Arrays.asList(review1, review2);

        when(reviewService.findAllReviews()).thenReturn(reviews);

        mockMvc.perform(get("/books/1/reviews/all"))
                .andExpect(status().isOk());
    }

    @Test
    void updateReview_ValidRequest_ReturnsUpdatedReview() throws Exception {
        Review updatedReview = new Review();
        updatedReview.setMessage("Updated review");

        when(reviewService.updateReview(eq(1), any(Review.class), eq(1L))).thenReturn(updatedReview);

        mockMvc.perform(put("/books/1/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReview)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReview_ValidRequest_ReturnsOk() throws Exception {
        doNothing().when(reviewService).deleteReview(1, 1L);

        mockMvc.perform(delete("/books/1/reviews/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createReviews_ValidRequest_ReturnsReviews() throws Exception {
        Review review1 = new Review();
        Review review2 = new Review();
        review1.setMessage("Good book!");
        review2.setMessage("Nice to read");
        List<Review> reviews = List.of(review1, review2);

        when(reviewService.createReview(eq(1L), any(Review.class)))
                .thenAnswer(inv -> inv.getArgument(1));

        mockMvc.perform(post("/books/1/reviews/b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviews)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
