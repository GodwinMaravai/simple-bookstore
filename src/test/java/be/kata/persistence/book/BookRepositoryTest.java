package be.kata.persistence.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        bookEntity = new BookEntity();
        bookEntity.setId("B1");
        bookEntity.setName("Book1");
        bookEntity.setAuthor("Author1");
        bookEntity.setCount(2);
        bookRepository.save(bookEntity);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        bookRepository.delete(bookEntity);
    }

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
