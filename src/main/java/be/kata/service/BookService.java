package be.kata.service;

import be.kata.api.model.Book;
import be.kata.persistence.book.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(entity -> new Book(entity.getId(), entity.getName(), entity.getAuthor(), entity.getCount()))
                .toList();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id)
                .map(entity -> new Book(entity.getId(), entity.getName(), entity.getAuthor(), entity.getCount()));
    }
}
