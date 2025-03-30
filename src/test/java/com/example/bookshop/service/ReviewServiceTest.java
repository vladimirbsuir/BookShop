package com.example.bookshop.service;

import com.example.bookshop.exception.ResourceNotFoundException;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void createReview_SaveReviewWithBook() {
        Long bookId = 1L;
        Book book = new Book();
        Review review = new Review();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(reviewRepository.save(review)).thenReturn(review);

        reviewService.createReview(bookId, review);

        assertSame(book, review.getBook());
        verify(reviewRepository).save(review);
    }

    @Test
    void createReview_ThrowWhenBookNotFound() {
        Long bookId = 0L;
        Review review = new Review();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> reviewService.createReview(bookId, review));

        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void updateReview_UpdateMessage() {
        Integer reviewId = 1;
        Long bookId = 1L;
        Review existingReview = new Review();
        existingReview.setMessage("Old message");
        Review updateRequest = new Review();
        updateRequest.setMessage("New message");

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(existingReview)).thenReturn(existingReview);

        Review result = reviewService.updateReview(reviewId, updateRequest, bookId);

        assertEquals("New message", result.getMessage());
        verify(reviewRepository).save(existingReview);
    }

    @Test
    void updateReview_ThrowWhenBookNotFound() {
        Long bookId = 0L;
        Review existingReview = new Review();
        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> reviewService.updateReview(1, existingReview, bookId));
    }

    @Test
    void deleteReview_CallDelete() {
        Integer reviewId = 1;
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        reviewService.deleteReview(reviewId, bookId);

        verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    void getReviewsByBookId_ReturnReviews() {
        Long bookId = 1L;
        List<Review> reviews = List.of(new Review(), new Review());
        when(reviewRepository.findByBookId(bookId)).thenReturn(reviews);

        List<Review> result = reviewService.getReviewsByBookId(bookId);

        assertEquals(2, result.size());
    }

    @Test
    void findAllReviews_ReturnAll() {
        List<Review> reviews = List.of(new Review(), new Review());
        when(reviewRepository.findAll()).thenReturn(reviews);

        List<Review> result = reviewService.findAllReviews();

        assertEquals(2, result.size());
    }

    @Test
    void updateReview_ThrowWhenReviewNotFound() {
        Integer reviewId = 0;
        Long bookId = 1L;
        Review review = new Review();
        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> reviewService.updateReview(reviewId, review, bookId));

        assertEquals("Review not found", exception.getMessage());
    }
}