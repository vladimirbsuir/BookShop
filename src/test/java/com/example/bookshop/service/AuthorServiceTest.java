package com.example.bookshop.service;

import com.example.bookshop.exception.ResourceNotFoundException;
import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.utils.CacheUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CacheUtil<Long, Author> authorCacheId;

    @Mock
    private CacheUtil<Long, Book> bookCacheId;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_Success() {
        Long authorId = 1L;
        Long bookId = 2L;
        Author author = new Author();
        author.setId(authorId);

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        Author result = authorService.findById(authorId, bookId);

        assertNotNull(result);
        assertEquals(authorId, result.getId());
        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    void testFindById_NotFound() {
        Long authorId = 1L;
        Long bookId = 2L;

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.findById(authorId, bookId));
        verify(authorRepository, times(1)).findById(authorId);
    }

    @Test
    void testSave_Success() {
        Author author = new Author();
        author.setName("John Doe");
        Long bookId = 1L;
        Book book = new Book();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.existsByName("John Doe")).thenReturn(false);
        when(authorRepository.save(author)).thenReturn(author);

        Author savedAuthor = authorService.save(author, bookId);

        assertNotNull(savedAuthor);
        assertEquals("John Doe", savedAuthor.getName());
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void testUpdate_Success() {
        Long authorId = 1L;
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Name");

        when(authorRepository.existsById(authorId)).thenReturn(true);
        when(authorRepository.save(updatedAuthor)).thenReturn(updatedAuthor);

        Author result = authorService.update(authorId, updatedAuthor);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(authorRepository, times(1)).save(updatedAuthor);
    }

    @Test
    void testDelete_Success() {
        Long authorId = 1L;
        Long bookId = 2L;
        Author author = new Author();
        author.setId(authorId);
        Book book = new Book();
        book.setAuthors(Arrays.asList(author));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        authorService.delete(authorId, bookId);

        verify(authorRepository, times(1)).delete(author);
        verify(authorCacheId, times(1)).remove(authorId);
    }
}
