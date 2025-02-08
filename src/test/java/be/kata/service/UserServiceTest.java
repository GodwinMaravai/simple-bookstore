package be.kata.service;

import be.kata.api.model.User;
import be.kata.persistence.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void givenUserService_whenInvokeMethod_thenReturnNoException() {
        assertThat(userService).isNotNull();
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.invokeMethod(userService, "isUserExist", 1L));
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.invokeMethod(userService, "createUser", new User(1, "", "", "")));
    }
}
