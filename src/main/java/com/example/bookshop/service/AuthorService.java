package com.example.bookshop.service;

import com.example.bookshop.model.Author;
import com.example.bookshop.model.Book;
import com.example.bookshop.repository.AuthorRepository;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.utils.CacheUtil;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/** Class to store business logic of the app. */
@Service
public class AuthorService {

    private static final String ERROR_MESSAGE = "Author not found";

    private final AuthorRepository authorRepository;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final CacheUtil<Long, Author> authorCacheId;
    private final CacheUtil<Long, Book> bookCacheId;

    /** Constructor to set authorRepository variable. */
    public AuthorService(AuthorRepository authorRepository, BookService bookService, BookRepository bookRepository,
                         CacheUtil<Long, Author> authorCacheId, CacheUtil<Long, Book> bookCacheId) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.authorCacheId = authorCacheId;
        this.bookCacheId = bookCacheId;
    }

    /** Function that returns author with certain id.
     *
     * @param id id of the author
     * @param bookId id of the book
     * @return JSON форму объекта Author
     * */
    public Author findById(Long id, Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book not found");
        }

        Book book = bookService.findById(bookId);
        List<Author> authors = book.getAuthors();
        Author author = authorCacheId.get(id);
        if (author == null) {
            author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE));
            authorCacheId.put(id, author);
        } else {
            System.out.println("Author was got from cache");
        }

        for (Author a : authors) {
            if (a.getId().equals(author.getId())) {
                return author;
            }
        }

        throw new EntityNotFoundException(ERROR_MESSAGE);
    }

    /** Function to get all authors from database.
     *
     * @return list pf authors
     */
    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    /** Function that save author in database.
     *
     * @param author объект класса Author
     * @return JSON форму объекта Author
     * */
    public Author save(Author author, Long bookId) {
        bookService.clearCache();
        Book book = bookService.findById(bookId);

        if (book == null) {
            throw new EntityNotFoundException("Book not found");
        }

        List<Book> newBooks = new ArrayList<>();

        if (authorRepository.existsByName(author.getName())) {
            author = authorRepository.findByName(author.getName());
            newBooks = author.getBooks();
        }

        List<Author> authors = book.getAuthors();
        if (!authors.contains(author)) {
            authors.add(author);
            book.setAuthors(authors);
            newBooks.add(book);
            author.setBooks(newBooks);
        }

        return authorRepository.save(author);
    }

    /** Function that updates info about author with certain id.
     *
     * @param id идентификатор объекта в базе данных
     * @param author объект класса Author
     * @return JSON форму объекта Author
     * */
    public Author update(Long id, Author author) {
        if (!authorCacheId.containsKey(id) && !authorRepository.existsById(id)) {
            throw new EntityNotFoundException(ERROR_MESSAGE);
        }

        author.setId(id);
        Author updatedAuthor = authorRepository.save(author);
        authorCacheId.clear();

        for (Map.Entry<Long, Book> bookEntry : bookCacheId.entrySet()) {
            Book book = bookEntry.getValue();
            List<Author> authors = book.getAuthors();
            if (authors.removeIf(auth -> auth.getId().equals(updatedAuthor.getId()))) {
                authors.add(updatedAuthor);
                book.setAuthors(authors);
                bookCacheId.put(bookEntry.getKey(), book);
            }
        }

        return updatedAuthor;
    }

    /** Function that deletes author with certain id. */
    public void delete(Long id, Long bookId) {
        bookService.clearCache();
        Book book = bookService.findById(bookId);
        List<Author> authors = book.getAuthors();
        Author author = findById(id, bookId);

        authors.remove(author);

        book.setAuthors(authors);
        bookService.update(bookId, book);

        List<Book> books = author.getBooks();
        books.remove(book);
        if (books.isEmpty()) {
            authorRepository.delete(author);
            authorCacheId.remove(id);
        } else {
            author.setBooks(books);
            update(id, author);
        }
    }
}