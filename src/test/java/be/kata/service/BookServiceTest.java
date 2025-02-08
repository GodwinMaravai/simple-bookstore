package be.kata.service;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class BookServiceTest {

    private BookService bookService = new BookService();

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
