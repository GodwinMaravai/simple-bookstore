package be.kata.service;

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
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.invokeMethod(userService, "login"));
        assertThatNoException().isThrownBy(() -> ReflectionTestUtils.invokeMethod(userService, "register"));
    }
}
