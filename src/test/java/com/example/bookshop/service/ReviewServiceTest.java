package com.example.bookshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.bookshop.controller.ReviewController;
import com.example.bookshop.mapper.ReviewMapper;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.repository.ReviewRepository;
import com.example.bookshop.utils.CacheUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookService bookService;

    @Mock
    private CacheUtil<Long, List<Review>> reviewCacheId;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CacheUtil<Long, Book> bookCacheId;

    @Mock
    ReviewMapper reviewMapper;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    ReviewController reviewController = new ReviewController(reviewService, reviewMapper);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReview() {
        Review review = new Review();
        reviewController.createReview(1L, review);
        verify(reviewService, times(1)).createReview(1L, review);
    }

    @Test
    void testUpdateReview() {
        Integer reviewId = 1;
        Long bookId = 1L;
        Review review = new Review();
        review.setMessage("Updated Review Message");
        Book book = new Book();
        book.setId(bookId);
        Review initialReview = new Review();
        initialReview.setId(reviewId.longValue());

        when(bookService.findById(bookId)).thenReturn(book);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(initialReview));
        when(reviewRepository.save(initialReview)).thenReturn(initialReview);

        Review result = reviewService.updateReview(reviewId, review, bookId);

        assertNotNull(result);
        assertEquals(review.getMessage(), result.getMessage());
        verify(reviewCacheId, times(1)).put(bookId, anyList());
    }

    @Test
    void testDeleteReview() {
        Integer reviewId = 1;
        Long bookId = 1L;

        reviewService.deleteReview(reviewId, bookId);

        verify(reviewRepository, times(1)).deleteById(reviewId);
        verify(reviewCacheId, times(1)).put(bookId, anyList());
    }

    @Test
    void testGetReviewsByBookId() {
        Long bookId = 1L;
        List<Review> reviews = Collections.singletonList(new Review());

        when(reviewCacheId.get(bookId)).thenReturn(null);
        when(reviewRepository.findByBookId(bookId)).thenReturn(reviews);

        List<Review> result = reviewService.getReviewsByBookId(bookId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reviewCacheId, times(1)).put(bookId, reviews);
    }
}