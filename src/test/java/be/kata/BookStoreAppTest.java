package be.kata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

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
    }
}
