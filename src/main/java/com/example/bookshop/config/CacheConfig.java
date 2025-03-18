package com.example.bookshop.config;

import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Review;
import com.example.bookshop.utils.CacheUtil;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Class to store cache. */
@Configuration
public class CacheConfig {

    /** Bean for book cache (requests by id). */
    @Bean
    public CacheUtil<Long, Book> bookCacheId() {
        return new CacheUtil<>(20);
    }

    /** Bean for author cache (requests by id). */
    @Bean
    public CacheUtil<Long, Author> authorCacheId() {
        return new CacheUtil<>(10);
    }

    /** Bean for review cache (requests by id). */
    @Bean
    public CacheUtil<Long, List<Review>> reviewCacheId() {
        return new CacheUtil<>(10);
    }
}
