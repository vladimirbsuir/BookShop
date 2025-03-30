package com.example.bookshop.dto;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class BookDtoTest {

    @Test
    void handleAllFields() {
        BookDto dto = new BookDto();

        dto.setTitle("1984");
        assertEquals("1984", dto.getTitle());

        AuthorDto author = new AuthorDto();
        author.setName("George Orwell");
        dto.setAuthors(List.of(author));

        assertEquals(1, dto.getAuthors().size());
        assertEquals("George Orwell", dto.getAuthors().getFirst().getName());

        ReviewDto review = new ReviewDto();
        review.setMessage("Classic");
        dto.setReviews(List.of(review));

        assertEquals(1, dto.getReviews().size());
        assertEquals("Classic", dto.getReviews().getFirst().getMessage());
    }

    @Test
    void handleNullCollections() {
        BookDto dto = new BookDto();
        dto.setAuthors(null);
        dto.setReviews(null);

        assertNull(dto.getAuthors());
        assertNull(dto.getReviews());
    }

    @Test
    void handleEmptyCollections() {
        BookDto dto = new BookDto();
        dto.setAuthors(Collections.emptyList());
        dto.setReviews(Collections.emptyList());

        assertTrue(dto.getAuthors().isEmpty());
        assertTrue(dto.getReviews().isEmpty());
    }
}
