package be.kata.service;

import be.kata.persistence.book.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class BookServiceTest {

    private BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private BookService bookService = new BookService(bookRepository);

    @Test
    void givenBookService_whenInvokeMethod_thenReturnNoException() {
        assertThat(bookService).isNotNull();
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.getField(bookService, "bookRepository"));
    }

    @Test
    void givenBookService_whenGetAllBooks_thenReturnAllBooks() {
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.invokeMethod(bookService, "getAllBooks"));
    }

    @Test
    void givenBookId_whenGetBookById_thenReturnABook() {
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.invokeMethod(bookService, "getBookById", "B1"));
    }
}
