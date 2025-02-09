package be.kata.service;

import be.kata.api.model.DisplayUser;
import be.kata.api.model.User;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import be.kata.security.BookStoreUserRole;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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
    void given_whenGetAllUser_thenReturnUser() {
        UserEntity user = new UserEntity();
        user.setName("adminuser");
        user.setPassword("pass");
        user.setRoles(BookStoreUserRole.ADMIN.name());

        when(userRepository.findAll()).thenReturn(List.of(user));

        assertThat(userService.getAllUser())
                .hasSize(1).satisfiesExactly(
                        item -> assertThat(item)
                                .returns(0L, DisplayUser::id)
                                .returns("adminuser", DisplayUser::name)
                                .returns(List.of(BookStoreUserRole.ADMIN), DisplayUser::roles));
        verify(userRepository).findAll();
    }

    @Test
    void givenUserName_whenFindUserByName_thenReturnUser() {
        UserEntity user = new UserEntity();
        user.setName("adminuser");
        user.setPassword("pass");
        user.setRoles(BookStoreUserRole.ADMIN.name());

        when(userRepository.findUserEntityByName("User1")).thenReturn(user);

        assertThat(userService.findUserByName("User1"))
                .returns("adminuser", User::name)
                .returns("pass", User::password)
                .returns(List.of(BookStoreUserRole.ADMIN), User::roles);
        verify(userRepository).findUserEntityByName("User1");
    }

    @Test
    void givenUser_whenCreateUser_thenReturnTrue() {
        User user = new User("User2", "pass", "1234567891", List.of(BookStoreUserRole.USER));
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        assertThat(userService.createUser(user)).isTrue();

        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(argumentCaptor.capture());
        UserEntity userEntity = argumentCaptor.getValue();
        assertThat(userEntity)
                .returns(0L, UserEntity::getId)
                .returns("User2", UserEntity::getName)
                .returns("1234567891", UserEntity::getNrn)
                .returns("encodedPass", UserEntity::getPassword)
                .returns(BookStoreUserRole.USER.name(), UserEntity::getRoles);
    }
}
