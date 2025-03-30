package com.example.bookshop.service;

import com.example.bookshop.exception.ResourceNotFoundException;
import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void findById_ReturnAuthorWhenExistsInBook() {
        Long authorId = 1L;
        Long bookId = 1L;
        Author author = new Author();
        Book book = new Book();
        book.setAuthors(List.of(author));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        Author result = authorService.findById(authorId, bookId);

        assertSame(author, result);
        verify(bookRepository).findById(bookId);
        verify(authorRepository).findById(authorId);
    }

    @Test
    void findById_ThrowWhenBookNotFound() {
        Long bookId = 0L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> authorService.findById(1L, bookId));

        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void save_AddNewAuthorToBook() {
        Long bookId = 1L;
        Author newAuthor = new Author();
        newAuthor.setName("New Author");
        Book book = new Book();
        book.setAuthors(List.of(newAuthor));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.existsByName("New Author")).thenReturn(false);
        when(authorRepository.save(newAuthor)).thenReturn(newAuthor);

        Author result = authorService.save(newAuthor, bookId);

        assertSame(newAuthor, result);
        assertTrue(book.getAuthors().contains(newAuthor));
        verify(authorRepository).save(newAuthor);
    }

    @Test
    void update_UpdateExistingAuthor() {
        Long authorId = 1L;
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Name");

        when(authorRepository.existsById(authorId)).thenReturn(true);
        when(authorRepository.save(updatedAuthor)).thenReturn(updatedAuthor);

        Author result = authorService.update(authorId, updatedAuthor);

        assertEquals(authorId, result.getId());
        assertEquals("Updated Name", result.getName());
        verify(authorRepository).save(updatedAuthor);
    }

    @Test
    void update_AuthorNotFound() {
        Long authorId = 0L;
        Author updatedAuthor = new Author();

        when(authorRepository.existsById(authorId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> authorService.update(authorId, updatedAuthor));
    }

    @Test
    void save_AddExistingAuthorToBookWhenNotPresent() {
        Long bookId = 1L;
        String authorName = "Existing Author";

        Author existingAuthor = new Author();
        existingAuthor.setId(1L);
        existingAuthor.setName(authorName);
        existingAuthor.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(bookId);
        book.setAuthors(new ArrayList<>());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.existsByName(authorName)).thenReturn(true);
        when(authorRepository.findByName(authorName)).thenReturn(existingAuthor);
        when(authorRepository.save(existingAuthor)).thenReturn(existingAuthor);

        Author newAuthor = new Author();
        newAuthor.setName(authorName);

        Author result = authorService.save(newAuthor, bookId);

        assertTrue(book.getAuthors().contains(existingAuthor));
        assertTrue(existingAuthor.getBooks().contains(book));
        verify(authorRepository).save(existingAuthor);
        assertSame(existingAuthor, result);
    }

    @Test
    void delete_RemoveAuthorWhenNoBooksLeft() {
        Long authorId = 1L;
        Long bookId = 1L;
        Author author = new Author();
        Book book = new Book();
        book.setAuthors(new ArrayList<>(List.of(author)));
        author.setBooks(new ArrayList<>(List.of(book)));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        authorService.delete(authorId, bookId);

        assertTrue(book.getAuthors().isEmpty());
        verify(authorRepository).delete(author);
    }

    @Test
    void findAllAuthors_ReturnAllAuthors() {
        List<Author> authors = List.of(new Author(), new Author());
        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> result = authorService.findAllAuthors();

        assertEquals(2, result.size());
        verify(authorRepository).findAll();
    }

    @Test
    void save_AddExistingAuthorToBook() {
        Long bookId = 1L;
        String authorName = "Existing Author";
        Author existingAuthor = new Author();
        existingAuthor.setName(authorName);
        Book book = new Book();
        book.setAuthors(List.of(existingAuthor));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.existsByName(authorName)).thenReturn(true);
        when(authorRepository.findByName(authorName)).thenReturn(existingAuthor);

        authorService.save(existingAuthor, bookId);

        assertTrue(book.getAuthors().contains(existingAuthor));
        verify(authorRepository).save(existingAuthor);
    }

    @Test
    void delete_ThrowWhenBookNotFound() {
        Long bookId = 0L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> authorService.delete(1L, bookId));

        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void delete_ThrowWhenAuthorNotFound() {
        Long authorId = 0L;
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> authorService.delete(authorId, bookId));

        assertEquals("Author not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
