package com.example.bookshop.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Class to store cache. */
@Configuration
@EnableCaching
public class CacheConfig {

    /** Function to create manager that will hold all app cache.
     *
     * @return object of CacheManager
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(20).expireAfterWrite(10, TimeUnit.MINUTES));
        return cacheManager;
    }
}
