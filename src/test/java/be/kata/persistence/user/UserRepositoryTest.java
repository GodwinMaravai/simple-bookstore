package be.kata.persistence.user;

import be.kata.persistence.cart.CartEntity;
import be.kata.persistence.cart.CartItemEntity;
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
class UserRepositoryTest {

    private UserEntity userEntity;
    private CartEntity cartEntity;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        userEntity = new UserEntity();
        userEntity.setName("User2");
        userEntity.setNrn("12345678902");

        CartItemEntity cartItemEntity1 = new CartItemEntity();
        cartItemEntity1.setBookId("B1");
        cartItemEntity1.setCount(1);

        CartItemEntity cartItemEntity2 = new CartItemEntity();
        cartItemEntity2.setBookId("B2");
        cartItemEntity2.setCount(2);

        cartEntity = new CartEntity();
        cartEntity.setItems(Set.of(cartItemEntity1, cartItemEntity2));
        userEntity.setCart(cartEntity);

        userRepository.save(userEntity);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        userRepository.delete(userEntity);
    }

    @Test
    void givenUserRepository_whenFindById_thenReturnUserEntity() {
        assertThat(userRepository.findById(1L))
                .isNotEmpty()
                .contains(userEntity);
    }

    @Test
    void givenUserRepository_whenFindUserEntityByName_thenReturnUserEntity() {
        assertThat(userRepository.findUserEntityByName("User2"))
                .isEqualTo(userEntity);
    }

    @Test
    void givenUserRepository_whenSave_thenReturnUserEntity() {
        userEntity = new UserEntity();
        userEntity.setName("USER");
        userEntity.setNrn("12345678901");

        assertThat(userRepository.save(userEntity))
                .isNotNull()
                .isEqualTo(userEntity);

        assertThat(userRepository.findById(userEntity.getId())).isNotNull();
    }

    @Test
    void givenUserWithCart_whenSave_thenReturnUserEntity() {
        userEntity = new UserEntity();
        userEntity.setName("USER");
        userEntity.setNrn("12345678901");
        userEntity.setCart(cartEntity);

        assertThat(userRepository.save(userEntity))
                .isNotNull()
                .isEqualTo(userEntity);

        assertThat(userRepository.findById(userEntity.getId())).isNotNull();
        assertThat(userRepository.findById(userEntity.getId()).get().getCart()).isEqualTo(cartEntity);
    }

}
