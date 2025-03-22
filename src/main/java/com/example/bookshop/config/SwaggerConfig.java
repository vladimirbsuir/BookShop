package com.example.bookshop.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration for swagger dependency. */
@Configuration
public class SwaggerConfig {

    public static final String CONTENT_TYPE = "application/json";
    public static final String COMPONENTS_SCHEMAS_ERROR_RESPONSE = "#/components/schemas/ErrorResponse";

    /** Component to initialize general information. */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components().schemas(Map.of(
                        "ErrorResponse", new Schema<>()
                                .description("Standard error response")
                                .addProperty("error", new Schema<String>()
                                        .description("Error message")
                                        .type("string")
                                )
                )))
                .info(new Info()
                        .title("BookShop API")
                        .description("API documentation for BookShop")
                        .version("1.0.0")
                )
                .servers(List.of(new Server().url("/")));
    }

    /** Component for API responses.
     *
     * @return API responses.
     */
    @Bean
    public ApiResponses apiResponses() {
        ApiResponses responses = new ApiResponses();

        responses.addApiResponse("400", new ApiResponse()
                .description("Bad Request")
                .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(CONTENT_TYPE,
                                new io.swagger.v3.oas.models.media.MediaType()
                                        .schema(new Schema<>()
                                                .$ref(COMPONENTS_SCHEMAS_ERROR_RESPONSE)))));

        responses.addApiResponse("404", new ApiResponse()
                .description("Resource Not Found")
                .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(CONTENT_TYPE,
                                new io.swagger.v3.oas.models.media.MediaType()
                                        .schema(new Schema<>()
                                                .$ref(COMPONENTS_SCHEMAS_ERROR_RESPONSE)))));

        responses.addApiResponse("500", new ApiResponse()
                .description("Internal Server Error")
                .content(new io.swagger.v3.oas.models.media.Content()
                        .addMediaType(CONTENT_TYPE,
                                new io.swagger.v3.oas.models.media.MediaType()
                                        .schema(new Schema<>()
                                                .$ref(COMPONENTS_SCHEMAS_ERROR_RESPONSE)))));
        return responses;
    }
}
