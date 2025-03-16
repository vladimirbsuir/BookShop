package com.example.bookshop.config;

import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Class to store cache. */
@Configuration
public class Cache {

    /** Bean for book cache (requests by id). */
    @Bean
    public Map<Long, Book> bookCacheId() {
        return new HashMap<>();
    }

    /** Bean for author cache (requests by id). */
    @Bean
    public Map<Long, Author> authorCacheId() {
        return new HashMap<>();
    }

    /** Bean for review cache (requests by id). */
    @Bean
    public Map<Long, List<Review>> reviewCacheId() {
        return new HashMap<>();
    }
}
