package com.example.bookshop.mapper;

import com.example.bookshop.dto.AuthorDto;
import com.example.bookshop.model.Author;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorMapperTest {

    private final AuthorMapper authorMapper = new AuthorMapper();

    @Test
    void toDto_MapAllFieldsCorrectly() {
        Author author = new Author();
        author.setName("J.R.R. Tolkien");

        AuthorDto dto = authorMapper.toDto(author);

        assertEquals(author.getName(), dto.getName());
    }
}
