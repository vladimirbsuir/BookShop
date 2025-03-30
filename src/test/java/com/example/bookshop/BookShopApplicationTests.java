package com.example.bookshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BookShopApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void loadApplicationContext() {
        assertNotNull(context, "Spring application context should be loaded");
    }

    @Test
    void main_StartApplication() {
        BookShopApplication.main(new String[] {});
        assertNotNull(context, "Application should be running after main() execution");
    }
}