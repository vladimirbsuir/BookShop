package com.example.bookshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.bookshop.controller.BookController;
import com.example.bookshop.mapper.BookMapper;
import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.utils.CacheUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

@WebMvcTest(BookController.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CacheUtil<Long, Book> bookCacheId;

    @Mock
    private CacheUtil<Long, List<Review>> reviewCacheId;

    @Mock
    private CacheUtil<Long, Author> authorCacheId;

    @Mock
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    BookMapper bookMapper;

    @InjectMocks
    BookController bookController = new BookController(bookService, bookMapper);

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFindById() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        when(bookCacheId.get(bookId)).thenReturn(null);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book result = bookService.findById(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        verify(bookCacheId, times(1)).put(bookId, book);
    }

    @Test
    void testSave() {
        Book book = new Book();
        book.setTitle("Book Title");

        bookController.createBook(book);
        verify(bookService, times(1)).save(book);
    }

    @Test
    void testUpdate() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Updated Book Title");

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.update(bookId, book);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        verify(bookCacheId, times(1)).put(bookId, book);
    }

    @Test
    void testDelete() {
        Long bookId = 1L;

        bookService.delete(bookId);

        verify(reviewCacheId, times(1)).remove(bookId);
        verify(bookCacheId, times(1)).remove(bookId);
        verify(authorCacheId, times(1)).clear();
        verify(bookRepository, times(1)).deleteById(bookId);
    }
}
