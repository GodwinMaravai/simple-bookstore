package be.kata.service;

import be.kata.api.model.User;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void givenUserName_whenIsUserExist_thenReturnTrue() {
        when(userRepository.findUserEntityByName("User1")).thenReturn(new UserEntity());

        assertThat(userService.isUserExist("User1")).isTrue();
        verify(userRepository).findUserEntityByName("User1");
    }

    @Test
    void givenUser_whenCreateUser_thenReturnTrue() {
        User user = new User("User2", "12345678902", "1234567891");

        assertThat(userService.createUser(user)).isTrue();

        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(argumentCaptor.capture());
        UserEntity userEntity = argumentCaptor.getValue();
        assertThat(userEntity)
                .returns(0L, UserEntity::getId)
                .returns("User2", UserEntity::getName)
                .returns("12345678902", UserEntity::getNrn)
                .returns("1234567891", UserEntity::getGsm);
    }
}
