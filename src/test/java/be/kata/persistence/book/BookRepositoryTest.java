package be.kata.persistence.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BookRepositoryTest {

    private BookEntity bookEntity;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void givenBookRepositorySetUp_whenFindAll_thenReturnBookEntity() {
        assertThat(bookRepository.findAll()).containsExactly(bookEntity);
    }

    @Test
    void givenBookRepositorySetUp_whenFindById_thenReturnBookEntity() {
        assertThat(bookRepository.findById("B1"))
                .isNotEmpty()
                .contains(bookEntity);
    }
}
