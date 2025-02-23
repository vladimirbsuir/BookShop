package com.example.bookshop.mapper;

import com.example.bookshop.dto.AuthorDto;
import com.example.bookshop.model.Author;
import org.springframework.stereotype.Component;

/** Class to transform object from dto and vice versa. */
@Component
public class AuthorMapper {

    /** Function to transform standard object to DTO.
     *
     * @param author object of Author class
     * @return DTO object
     */
    public AuthorDto toDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(author.getName());
        return authorDto;
    }

    /** Function to transform DTO to standard object.
     *
     * @param authorDto object of AuthorDto object
     * @return standard Author object
     */
    public Author toEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.getName());
        return author;
    }
}