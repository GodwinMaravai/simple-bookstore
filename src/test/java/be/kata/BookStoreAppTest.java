package be.kata;

import be.kata.api.BookController;
import be.kata.api.CartController;
import be.kata.api.UserController;
import be.kata.service.BookService;
import be.kata.service.CartService;
import be.kata.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
class BookStoreAppTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void givenApplicationContext_whenGetBean_thenNotNullAndDependenciesNotNull() {
        assertThat(context.getBean("bookStoreApp")).isNotNull();

        assertThat(context.getBean("bookRepository")).isNotNull();
        assertThat(context.getBean("bookService")).isNotNull();
        assertThat(context.getBean("bookController")).isNotNull();

        BookService bookService = (BookService) context.getBean("bookService");
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.getField(bookService, "bookRepository"));

        BookController bookController = (BookController) context.getBean("bookController");
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.getField(bookController, "bookService"));

        assertThat(context.getBean("userRepository")).isNotNull();
        assertThat(context.getBean("userService")).isNotNull();
        assertThat(context.getBean("userController")).isNotNull();

        UserService userService = (UserService) context.getBean("userService");
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.getField(userService, "userRepository"));

        UserController userController = (UserController) context.getBean("userController");
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.getField(userController, "userService"));

        assertThat(context.getBean("cartService")).isNotNull();
        assertThat(context.getBean("cartController")).isNotNull();

        CartService cartService = (CartService) context.getBean("cartService");
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.getField(cartService, "userRepository"));
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.getField(cartService, "bookRepository"));

        CartController cartController = (CartController) context.getBean("cartController");
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.getField(cartController, "cartService"));

        assertThat(context.getBean("orderRepository")).isNotNull();
        assertThat(context.getBean("orderService")).isNotNull();
        assertThat(context.getBean("orderController")).isNotNull();
    }
}
