package com.example.bookshop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration for swagger dependency. */
@Configuration
public class SwaggerConfig {

    /** Component to initialize openAPI configuration. */
    @Bean
    public OpenAPI openApiConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookShop API")
                        .version("1.0")
                        .description("API for interacting with books and their elements (authors and reviews)"));
    }
}
