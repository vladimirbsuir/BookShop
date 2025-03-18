package com.example.bookshop.service;

import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.utils.Cache;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/** Class to store business logic of the app. */
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final Cache<Long,  Book> bookCacheId;
    private final Cache<Long, List<Review>> reviewCacheId;
    private final Cache<Long, Author> authorCacheId;

    /**
     * Constructor to set bookRepository variable.
     *
     * @param bookRepository объект класса BookRepository
     * */
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, Cache<Long, Book> bookCacheId,
                       Cache<Long, List<Review>> reviewCacheId, Cache<Long, Author> authorCacheId) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookCacheId = bookCacheId;
        this.reviewCacheId = reviewCacheId;
        this.authorCacheId = authorCacheId;
    }

    /** Function that returns books which contains substring "title".
     *
     * @param title название книги
     * @return JSON форму объекта Book
     * */
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    /** Function to get all books from database.
     *
     * @return all books in database
     */
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    /** Function that returns book with certain id.
     *
     * @param id идентификатор книги в базе данных
     * @return JSON форму объекта Book
     * */
    public Book findById(Long id) {
        Book cachedBook = bookCacheId.get(id);
        if (cachedBook != null) {
            System.out.println("Book was got from cache");
            return cachedBook;
        }

        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        bookCacheId.put(id, book);

        return book;
    }

    /** Function that returns books with specified author.
     *
     * @param authorName name of the author
     * @return list of books with specified author
     */
    public List<Book> findByAuthorName(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    /** Function to get books with amount of reviews greater than reviewCount.
     *
     * @param reviewCount amount of reviews
     * @return list of books
     */
    public List<Book> findByReviewCount(Long reviewCount) {
        return bookRepository.findByReviewCount(reviewCount);
    }

    /** Function that saves book in database.
     *
     * @param book объек класса Book
     * @return JSON форму объекта Book
     * */
    public Book save(Book book) {
        if (book.getAuthors() != null) {
            List<Author> savedAuthors = new ArrayList<>();

            for (Author author : book.getAuthors()) {

                if (authorRepository.existsByName(author.getName())) {
                    Author existingAuthor = authorRepository.findByName(author.getName());
                    savedAuthors.add(existingAuthor);
                } else {
                    savedAuthors.add(authorRepository.save(author));
                }
            }

            book.setAuthors(savedAuthors);
        }

        if (book.getReviews() != null) {
            for (Review review : book.getReviews()) {
                review.setBook(book);
            }
        }

        return bookRepository.save(book);
    }

    /** Function that updates info about book.
     *
     * @param id идентификатор книги
     * @param book объект класса Book
     * @return JSON форму объекта Book
     * */
    public Book update(Long id, Book book) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found");
        }

        Book existsBook = findById(id);
        book.setAuthors(existsBook.getAuthors());
        book.setReviews(existsBook.getReviews());

        book.setId(id);

        Book updatedBook = bookRepository.save(book);
        if (bookCacheId.containsKey(id)) {
            bookCacheId.put(id, updatedBook);
        }

        return updatedBook;
    }

    /**
     * Function that removes book from database.
     *
     * @param id идентификатор объекта в базе данных
     * */
    public void delete(Long id) {
        reviewCacheId.remove(id);
        bookCacheId.remove(id);
        authorCacheId.clear();
        bookRepository.deleteById(id);
    }

    /** Function to clear book cache. */
    public void clearCache() {
        bookCacheId.clear();
    }
}
