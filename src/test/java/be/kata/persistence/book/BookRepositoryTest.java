package be.kata.persistence.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void givenBookRepository_whenInvokeMethod_thenReturn_findAllMethod() {
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.invokeMethod(bookRepository, "findAll"));
    }

    @Test
    void givenBookRepository_whenInvokeMethod_thenReturn_findByIdMethod() {
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.invokeMethod(bookRepository, "findById", "B1"));
    }
}
