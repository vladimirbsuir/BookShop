package com.example.bookshop.controller;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.mapper.BookMapper;
import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Configuration
    static class TestConfiguration {
        @Bean
        public BookController bookController() {
            return new BookController(bookService(), bookMapper());
        }

        @Bean
        public BookService bookService() {
            return mock(BookService.class);
        }

        @Bean
        public BookMapper bookMapper() {
            return mock(BookMapper.class);
        }
    }

    @Test
    void getBookById_ValidRequest_ReturnsBookDto() throws Exception {
        Book book = new Book();
        book.setTitle("Java");
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Java");

        when(bookService.findById(1L)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookByTitle_ValidRequest_ReturnsBookDto() throws Exception {
        Book book = new Book();
        book.setTitle("Java");
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Java");

        when(bookService.findByTitle("Java")).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        mockMvc.perform(get("/books?title=Java"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBooks_ValidRequest_ReturnsBookDto() throws Exception {
        Book book1 = new Book();
        book1.setTitle("Java");
        Book book2 = new Book();
        book2.setTitle("Spring");
        List<Book> books = Arrays.asList(book1, book2);

        when(bookService.findAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getBooksByAuthorName_ValidRequest_ReturnsBookDto() throws Exception {
        Book book = new Book();
        book.setTitle("Java");
        Author author = new Author();
        author.setName("John");
        book.setAuthors(List.of(author));

        when(bookService.findByAuthorName("John")).thenReturn(List.of(book));

        mockMvc.perform(get("/books/find?authorName=John"))
                .andExpect(status().isOk());
    }

    @Test
    void getBooksByReviewCount_ValidRequest_ReturnsBookDto() throws Exception {
        Book book = new Book();
        book.setTitle("Java");
        Review review = new Review();
        book.setReviews(List.of(review));

        when(bookService.findByReviewCount(0L)).thenReturn(List.of(book));

        mockMvc.perform(get("/books/find/reviews?reviewCount=0"))
                .andExpect(status().isOk());
    }

    @Test
    void createBook_ValidRequest_ReturnsBook() throws Exception {
        Book book = new Book();
        book.setTitle("Spring");

        when(bookService.save(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk());
    }

    @Test
    void updateBook_ValidRequest_ReturnsBook() throws Exception {
        Book updatedBook = new Book();
        updatedBook.setTitle("New title");

        when(bookService.update(eq(1L), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk());
    }
}