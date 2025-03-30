package com.example.bookshop.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import com.example.bookshop.dto.AuthorDto;
import com.example.bookshop.dto.BookDto;
import com.example.bookshop.dto.ReviewDto;
import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private BookMapper bookMapper;

    @Test
    void toDto_MapAllFieldsWithRelations() {
        Book book = new Book();
        book.setTitle("The Lord of the Rings");

        Author author = new Author();
        author.setName("J.R.R. Tolkien");
        book.setAuthors(List.of(author));

        Review review = new Review();
        review.setMessage("Masterpiece");
        book.setReviews(List.of(review));

        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(author.getName());

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setMessage(review.getMessage());

        when(authorMapper.toDto(any(Author.class))).thenReturn(authorDto);
        when(reviewMapper.toDto(any(Review.class))).thenReturn(reviewDto);

        BookDto dto = bookMapper.toDto(book);

        assertEquals(book.getTitle(), dto.getTitle());
        assertEquals(1, dto.getAuthors().size());
        assertEquals(author.getName(), dto.getAuthors().getFirst().getName());
        assertEquals(1, dto.getReviews().size());
        assertEquals(review.getMessage(), dto.getReviews().getFirst().getMessage());

        verify(authorMapper).toDto(author);
        verify(reviewMapper).toDto(review);
    }

    @Test
    void toDto_HandleNullRelations() {
        Book book = new Book();
        book.setTitle("Untitled Book");
        book.setAuthors(null);
        book.setReviews(null);

        BookDto dto = bookMapper.toDto(book);

        assertEquals(book.getTitle(), dto.getTitle());
        assertNull(dto.getAuthors());
        assertNull(dto.getReviews());
    }

    @Test
    void toDto_ShouldHandleEmptyRelations() {
        Book book = new Book();
        book.setTitle("Empty Book");
        book.setAuthors(List.of());
        book.setReviews(List.of());

        BookDto dto = bookMapper.toDto(book);

        assertEquals(book.getTitle(), dto.getTitle());
        assertTrue(dto.getAuthors().isEmpty());
        assertTrue(dto.getReviews().isEmpty());
    }
}