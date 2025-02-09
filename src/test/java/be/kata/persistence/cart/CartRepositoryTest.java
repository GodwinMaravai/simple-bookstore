package be.kata.persistence.cart;

import be.kata.persistence.book.BookEntity;
import be.kata.persistence.book.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CartRepositoryTest {

    private CartEntity cartEntity;
    private BookEntity bookEntity1;
    private BookEntity bookEntity2;
    private CartItemEntity cartItemEntity1;
    private CartItemEntity cartItemEntity2;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        cartEntity = new CartEntity();

        bookEntity1 = new BookEntity();
        bookEntity1.setId("B1");
        bookEntity1.setName("Book1");
        bookEntity1.setAuthor("Author1");
        bookEntity1.setPrice(1);
        bookEntity1.setCount(2);
        bookRepository.save(bookEntity1);

        bookEntity2 = new BookEntity();
        bookEntity2.setId("B2");
        bookEntity2.setName("Book2");
        bookEntity2.setAuthor("Author2");
        bookEntity2.setPrice(2);
        bookEntity2.setCount(2);
        bookRepository.save(bookEntity2);

        cartItemEntity1 = new CartItemEntity();
        cartItemEntity1.setBook(bookEntity1);
        cartItemEntity1.setCount(1);

        cartItemEntity2 = new CartItemEntity();
        cartItemEntity2.setBook(bookEntity2);
        cartItemEntity2.setCount(2);

        cartEntity.setItems(Set.of(cartItemEntity1, cartItemEntity2));
        cartEntity.setUserId(1L);
        cartRepository.save(cartEntity);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        cartRepository.delete(cartEntity);
    }

    @Test
    void givenCartRepository_whenFindById_thenReturnCart() {
        assertThat(cartRepository.findById(1L))
                .isNotEmpty()
                .contains(cartEntity);
    }

    @Test
    void givenCartRepository_whenSave_thenReturnCartEntity() {
        cartEntity = new CartEntity();
        cartEntity.setUserId(2L);
        cartEntity.setItems(Set.of(cartItemEntity1));

        assertThat(cartRepository.save(cartEntity))
                .isNotNull()
                .isEqualTo(cartEntity);

        assertThat(cartRepository.findById(cartEntity.getId())).isNotNull();
    }
}
