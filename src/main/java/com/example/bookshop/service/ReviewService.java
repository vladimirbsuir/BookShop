package com.example.bookshop.service;

import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.repository.ReviewRepository;
import com.example.bookshop.utils.Cache;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/** Class that make CRUD operations with Review object. */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookService bookService;
    private final Cache<Long, List<Review>> reviewCacheId;
    private final BookRepository bookRepository;
    private final Cache<Long, Book> bookCacheId;

    /** Constructor of the class.
     *
     * @param reviewRepository object of the ReviewRepository class
     * @param bookService object of the BookRepository class
     */
    public ReviewService(ReviewRepository reviewRepository, BookService bookService, Cache<Long, List<Review>> reviewCacheId,
                         BookRepository bookRepository, Cache<Long, Book> bookCacheId) {
        this.reviewRepository = reviewRepository;
        this.bookService = bookService;
        this.reviewCacheId = reviewCacheId;
        this.bookRepository = bookRepository;
        this.bookCacheId = bookCacheId;
    }

    /** Function to add review to the book.
     *
     * @param bookId id of the book
     * @param review object of the Review class
     * @return created review
     */
    public Review createReview(Long bookId, Review review) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        review.setBook(book);

        Book cachedBook = bookCacheId.get(bookId);
        if (cachedBook != null) {
            List<Review> reviews = cachedBook.getReviews();
            reviews.add(review);
            cachedBook.setReviews(reviews);
            bookCacheId.put(bookId, cachedBook);
        }

        reviewCacheId.clear();

        return reviewRepository.save(review);
    }

    /** Function to update review of the book.
     *
     * @param reviewId id of the review
     * @param review object of the Review class
     * @param bookId id of the book
     * @return updated review
     */
    public Review updateReview(Integer reviewId, Review review, Long bookId) {
        Book book = bookService.findById(bookId);
        if (book == null) {
            book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        }

        List<Review> reviews = book.getReviews();
        Review initialReview = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        for (Review r : reviews) {
            if (r.getId().equals(Long.valueOf(reviewId))) {
                initialReview.setMessage(review.getMessage());
                Review updatedReview = reviewRepository.save(initialReview);

                List<Review> cachedReviews = reviewCacheId.get(bookId);
                if (cachedReviews != null) {
                    cachedReviews.removeIf(rev -> rev.getId().equals(Long.valueOf(reviewId)));
                    cachedReviews.add(updatedReview);
                    reviewCacheId.put(bookId, cachedReviews);
                }

                Book cachedBook = bookCacheId.get(bookId);
                if (cachedBook != null) {
                    if (cachedReviews == null) {
                        cachedReviews = cachedBook.getReviews();
                        cachedReviews.removeIf(rev -> rev.getId().equals(Long.valueOf(reviewId)));
                        cachedReviews.add(updatedReview);
                    }
                    cachedBook.setReviews(cachedReviews);
                    bookCacheId.put(bookId, cachedBook);
                }

                return updatedReview;
            }
        }

        throw new EntityNotFoundException("Review not found");
    }

    /** Function to delete review.
     *
     * @param reviewId id of the review
     */
    public void deleteReview(Integer reviewId, Long bookId) {
        reviewRepository.deleteById(reviewId);
        List<Review> reviews = reviewCacheId.get(bookId);
        if (reviews != null) {
            reviews.removeIf(rev -> rev.getId().equals(Long.valueOf(reviewId)));
            reviewCacheId.put(bookId, reviews);
        }

        Book book = bookCacheId.get(bookId);
        if (book != null) {
            if (reviews == null) {
                reviews = book.getReviews();
                reviews.removeIf(rev -> rev.getId().equals(Long.valueOf(reviewId)));
            }
            book.setReviews(reviews);
            bookCacheId.put(bookId, book);
        }
    }

    /** Function to get all reviews of the book.
     *
     * @param bookId id of the book
     * @return reviews of the book
     */
    public List<Review> getReviewsByBookId(Long bookId) {
        List<Review> reviews = reviewCacheId.get(bookId);
        if (reviews != null) {
            System.out.println("Reviews was got from cache");
            return reviews;
        }

        reviews = reviewRepository.findByBookId(bookId);
        reviewCacheId.put(bookId, reviews);

        return reviews;
    }

    /** Function to get all reviews from database.
     *
     * @return list of reviews
     */
    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }
}