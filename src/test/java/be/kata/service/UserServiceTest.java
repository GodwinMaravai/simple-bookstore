package be.kata.service;

import be.kata.api.model.User;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import be.kata.persistence.user.UserRole;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @Test
    void givenUserName_whenIsUserExist_thenReturnTrue() {
        when(userRepository.findUserEntityByName("User1")).thenReturn(new UserEntity());

        assertThat(userService.isUserExist("User1")).isTrue();
        verify(userRepository).findUserEntityByName("User1");
    }

    @Test
    void givenUserName_whenFindUserByName_thenReturnUser() {
        UserEntity user = new UserEntity();
        user.setName("adminuser");
        user.setPassword("pass");
        user.setRole(UserRole.ADMIN);

        when(userRepository.findUserEntityByName("User1")).thenReturn(user);

        assertThat(userService.findUserByName("User1"))
                .returns("adminuser", User::name)
                .returns("pass", User::password)
                .returns(UserRole.ADMIN, User::role);
        verify(userRepository).findUserEntityByName("User1");
    }

    @Test
    void givenUser_whenCreateUser_thenReturnTrue() {
        User user = new User("User2", "pass", "1234567891", UserRole.USER);

        assertThat(userService.createUser(user)).isTrue();

        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(argumentCaptor.capture());
        UserEntity userEntity = argumentCaptor.getValue();
        assertThat(userEntity)
                .returns(0L, UserEntity::getId)
                .returns("User2", UserEntity::getName)
                .returns("1234567891", UserEntity::getNrn)
                .returns("pass", UserEntity::getPassword)
                .returns(UserRole.USER, UserEntity::getRole);
    }
}
