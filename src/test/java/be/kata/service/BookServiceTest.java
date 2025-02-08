package be.kata.service;

import be.kata.api.model.Book;
import be.kata.persistence.book.BookEntity;
import be.kata.persistence.book.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServiceTest {

    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final BookService bookService = new BookService(bookRepository);

    @Test
    void givenBookService_whenInvokeMethod_thenReturnNoException() {
        assertThat(bookService).isNotNull();
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.getField(bookService, "bookRepository"));
    }

    @Test
    void givenBookService_whenGetAllBooks_thenReturnAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(new BookEntity(), new BookEntity()));

        assertThat(bookService.getAllBooks()).isNotNull().hasSize(2);
        verify(bookRepository).findAll();
    }

    @Test
    void givenBookId_whenGetBookById_thenReturnABook() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId("B1");
        bookEntity.setName("Book1");
        bookEntity.setAuthor("Author1");
        bookEntity.setCount(2);
        when(bookRepository.findById("B1")).thenReturn(Optional.of(bookEntity));

        Book book = new Book("B1", "Book1", "Author1", 2);

        assertThat(bookService.getBookById("B1")).isNotNull().hasValue(book);
        verify(bookRepository).findById("B1");
    }
}
