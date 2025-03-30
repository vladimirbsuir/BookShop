package com.example.bookshop.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AuthorDtoTest {

    @Test
    void handleName() {
        AuthorDto dto = new AuthorDto();

        assertNull(dto.getName());

        dto.setName("George Orwell");
        assertEquals("George Orwell", dto.getName());

        dto.setName(null);
        assertNull(dto.getName());
    }
}