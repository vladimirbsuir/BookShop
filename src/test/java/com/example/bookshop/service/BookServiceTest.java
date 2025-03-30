package com.example.bookshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.example.bookshop.exception.ResourceNotFoundException;
import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void testFindById_ReturnsBook() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Title");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book result = bookService.findById(bookId);
        assertEquals(bookId, result.getId());
        verify(bookRepository).findById(bookId);
    }

    @Test
    void save_SetBookForReviewsWhenReviewsExist() {
        Book book = new Book();
        book.setTitle("Book with Reviews");

        Review review1 = new Review();
        Review review2 = new Review();
        book.setReviews(List.of(review1, review2));

        when(bookRepository.save(book)).thenReturn(book);

        bookService.save(book);

        assertAll(
                () -> assertEquals(book, review1.getBook()),
                () -> assertEquals(book, review2.getBook())
        );
        verify(bookRepository).save(book);
    }

    @Test
    void findByAuthorName_ReturnBooksForAuthor() {
        String authorName = "Good man";
        Book book1 = new Book();
        book1.setTitle("Title1");
        Book book2 = new Book();
        book2.setTitle("Title 2");

        when(bookRepository.findByAuthorName(authorName)).thenReturn(List.of(book1, book2));

        List<Book> result = bookService.findByAuthorName(authorName);

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.contains(book1)),
                () -> assertTrue(result.contains(book2))
        );
        verify(bookRepository).findByAuthorName(authorName);
    }

    @Test
    void testFindByReviewCount_ReturnsBook() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Title");
        book.setReviews(List.of(new Review()));

        when(bookRepository.findByReviewCount(0L)).thenReturn(List.of(book));

        List<Book> result = bookService.findByReviewCount(0L);
        assertTrue(result.contains(book));
        verify(bookRepository).findByReviewCount(0L);
    }

    @Test
    void findByTitle_ReturnsBook() {
        String title = "Java";
        Book expectedBook = new Book();
        expectedBook.setTitle(title);

        when(bookRepository.findByTitle(title)).thenReturn(expectedBook);

        Book result = bookService.findByTitle(title);

        assertEquals(title, result.getTitle());
        assertSame(expectedBook, result);
    }

    @Test
    void testSave_ReturnsBook() {
        Book book = new Book();
        book.setTitle("Book Title");

        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.save(book);

        assertEquals(book.getId(), result.getId());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void save_UseExistingAuthors() {
        Book book = new Book();
        Author existingAuthor = new Author();
        existingAuthor.setName("Existing Author");
        book.setAuthors(List.of(existingAuthor));

        when(authorRepository.existsByName("Existing Author")).thenReturn(true);
        when(authorRepository.findByName("Existing Author")).thenReturn(existingAuthor);
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.save(book);

        verify(authorRepository, never()).save(existingAuthor);
        assertSame(book, result);
    }

    @Test
    void findAllBooks_ReturnAllBooks() {
        List<Book> books = List.of(new Book(), new Book());

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.findAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    void testUpdate_ReturnsBook() {
        Long bookId = 1L;
        Book oldBook = new Book();
        oldBook.setId(bookId);
        oldBook.setTitle("Old Title");
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Updated title");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(oldBook));
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.update(bookId, book);

        assertNotEquals(oldBook.getTitle(), result.getTitle());
        assertEquals(bookId, result.getId());
        assertSame(book, result);
    }

    @Test
    void findById_ThrowWhenNotFound() {
        Long id = 0L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bookService.findById(id));

        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testDelete() {
        Long bookId = 1L;

        bookService.delete(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }
}
