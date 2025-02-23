package com.example.bookshop.mapper;

import com.example.bookshop.dto.AuthorDto;
import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.ReviewDto;
import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/** Class to transform object from dto and vice versa. */
@Component
public class BookMapper {
    private final AuthorMapper authorMapper;
    private final ReviewMapper reviewMapper;

    /** Constructor of the class. */
    public BookMapper(AuthorMapper authorMapper, ReviewMapper reviewMapper) {
        this.authorMapper = authorMapper;
        this.reviewMapper = reviewMapper;
    }

    /** Function to transform standard object to DTO.
     *
     * @param book object of the Book class
     * @return dto object
     */
    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(book.getTitle());

        if (book.getAuthors() != null) {
            List<AuthorDto> authorsDto = book.getAuthors().stream()
                    .map(authorMapper::toDto)
                    .collect(Collectors.toList());
            bookDto.setAuthors(authorsDto);
        }

        if (book.getReviews() != null) {
            List<ReviewDto> reviewsDto = book.getReviews().stream()
                    .map(reviewMapper::toDto)
                    .collect(Collectors.toList());
            bookDto.setReviews(reviewsDto);
        }

        return bookDto;
    }

    /** Function to transform DTO to standard object.
     *
     * @param bookDto object of the BookDto class
     * @return standard Book object
     */
    public Book toEntity(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());

        if (bookDto.getAuthors() != null) {
            List<Author> authors = bookDto.getAuthors().stream()
                    .map(authorMapper::toEntity)
                    .collect(Collectors.toList());
            book.setAuthors(authors);
        }

        if (bookDto.getReviews() != null) {
            List<Review> reviews = bookDto.getReviews().stream()
                    .map(reviewMapper::toEntity)
                    .collect(Collectors.toList());
            book.setReviews(reviews);
        }

        return book;
    }
}