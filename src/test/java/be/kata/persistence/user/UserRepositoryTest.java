package be.kata.persistence.user;

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
class UserRepositoryTest {

    private UserEntity userEntity;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        userEntity = new UserEntity();
        userEntity.setName("User2");
        userEntity.setNrn("12345678902");
        userEntity.setGsm("1234567891");
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
    void givenUserRepository_whenSave_thenReturnUserEntity() {
        userEntity.setName("USER");
        userEntity.setNrn("12345678901");
        userEntity.setGsm("1234567890");

        assertThat(userRepository.save(userEntity))
                .isNotNull()
                .isEqualTo(userEntity);

        assertThat(userRepository.findById(userEntity.getId())).isNotNull();
    }
}
