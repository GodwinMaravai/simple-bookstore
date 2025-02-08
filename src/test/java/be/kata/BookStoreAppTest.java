package be.kata;

import be.kata.api.BookController;
import be.kata.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookStoreAppTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void givenApplicationContext_whenGetBean_thenNotNull() {
        assertThat(context.getBean("bookStoreApp")).isNotNull();

        assertThat(context.getBean("bookController")).isNotNull();
        assertThat(context.getBean("bookService")).isNotNull();
        assertThat(context.getBean("bookRepository")).isNotNull();

        BookService bookService = (BookService) context.getBean("bookService");
        ReflectionTestUtils.getField(bookService, "bookRepository");

        BookController bookController = (BookController) context.getBean("bookController");
        ReflectionTestUtils.getField(bookController, "bookService");

        assertThat(context.getBean("userRepository")).isNotNull();
        assertThat(context.getBean("userService")).isNotNull();
        assertThat(context.getBean("userController")).isNotNull();
    }
}
