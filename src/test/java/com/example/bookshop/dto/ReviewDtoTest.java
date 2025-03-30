package com.example.bookshop.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ReviewDtoTest {

    @Test
    void handleMessageCorrectly() {
        ReviewDto dto = new ReviewDto();

        assertNull(dto.getMessage());

        dto.setMessage("Great book!");
        assertEquals("Great book!", dto.getMessage());

        dto.setMessage(null);
        assertNull(dto.getMessage());
    }
}
