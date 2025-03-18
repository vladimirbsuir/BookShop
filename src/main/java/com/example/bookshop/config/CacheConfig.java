package com.example.bookshop.config;

import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.utils.Cache;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Class to store cache. */
@Configuration
public class CacheConfig {

    /** Bean for book cache (requests by id). */
    @Bean
    public Cache<Long, Book> bookCacheId() {
        return new Cache<>(20);
    }

    /** Bean for author cache (requests by id). */
    @Bean
    public Cache<Long, Author> authorCacheId() {
        return new Cache<>(10);
    }

    /** Bean for review cache (requests by id). */
    @Bean
    public Cache<Long, List<Review>> reviewCacheId() {
        return new Cache<>(10);
    }
}
