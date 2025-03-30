package com.example.bookshop.controller;

import com.example.bookshop.exception.ResourceNotFoundException;
import com.example.bookshop.model.Author;
import com.example.bookshop.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthorService authorService;
    @InjectMocks
    private AuthorController authorController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
    }

    @Test
    void createAuthor_ValidRequest_ReturnsAuthor() throws Exception {
        Author author = new Author();
        author.setName("John Doe");

        when(authorService.save(any(Author.class), eq(1L))).thenReturn(author);

        mockMvc.perform(post("/books/1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk());
    }

    @Test
    void updateAuthor_ValidRequest_ReturnsUpdatedAuthor() throws Exception {
        Author updatedAuthor = new Author();
        updatedAuthor.setName("John Doe");

        when(authorService.update(eq(1L), any(Author.class))).thenReturn(updatedAuthor);

        mockMvc.perform(put("/books/1/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthor)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAuthor_ValidRequest_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/1/authors/1"))
                .andExpect(status().isOk());

        verify(authorService).delete(1L, 1L);
    }

    @Test
    void getAuthorById_ValidRequest_ReturnsAuthor() throws Exception {
        Author author = new Author();
        author.setName("John Doe");

        when(authorService.findById(1L, 1L)).thenReturn(author);

        mockMvc.perform(get("/books/1/authors/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllAuthors_ValidRequest_ReturnsAuthors() throws Exception {
        mockMvc.perform(get("/books/1/authors/all"))
                .andExpect(status().isOk());

        verify(authorService).findAllAuthors();
    }

    @Test
    void createAuthor_InvalidName_ReturnsBadRequest() throws Exception {
        Author invalidAuthor = new Author();
        invalidAuthor.setName("");

        mockMvc.perform(post("/books/1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAuthor)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAuthorById_NonExistingId_ReturnsNotFound() {
        when(authorService.findById(999L, 1L))
                .thenThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Author not found"));

        assertThrows(ResourceNotFoundException.class, () -> authorController.findByBookId(999L, 1L));
    }
}