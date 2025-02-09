package be.kata.service;

import be.kata.api.model.Book;
import be.kata.persistence.book.BookEntity;
import be.kata.persistence.book.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(entity -> new Book(entity.getId(), entity.getName(), entity.getAuthor(), entity.getPrice(), entity.getCount()))
                .toList();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id)
                .map(entity -> new Book(entity.getId(), entity.getName(), entity.getAuthor(), entity.getPrice(), entity.getCount()));
    }

    public boolean submit(List<Book> books) {
        Set<BookEntity> bookEntities = books.stream().map(book -> {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setId(book.id());
            bookEntity.setName(book.title());
            bookEntity.setAuthor(book.author());
            bookEntity.setPrice(book.totalPrice());
            bookEntity.setCount(book.count());
            return bookEntity;
        }).collect(Collectors.toSet());
        bookRepository.saveAll(bookEntities);
        return true;
    }
}
