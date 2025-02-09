package be.kata.service;

import be.kata.api.model.DisplayUser;
import be.kata.api.model.User;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import be.kata.security.BookStoreUserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUserExist(String name) {
        return !isEmpty(userRepository.findUserEntityByName(name));
    }

    public List<DisplayUser> getAllUser() {
        List<UserEntity> userEntities = (List<UserEntity>) userRepository.findAll();
        return userEntities.stream()
                .map(userEntity -> new DisplayUser(userEntity.getId(), userEntity.getName(), roles(userEntity.getRoles())))
                .toList();
    }

    public User findUserByName(String name) {
        UserEntity userEntity = userRepository.findUserEntityByName(name);
        return new User(userEntity.getName(), userEntity.getPassword(), userEntity.getNrn(), roles(userEntity.getRoles()));
    }

    public boolean createUser(User user) {
        Assert.notNull(user, "Invalid name");
        UserEntity userEntity = new UserEntity();
        userEntity.setNrn(user.nrn());
        userEntity.setName(user.name());
        userEntity.setPassword(passwordEncoder.encode(user.password()));
        userEntity.setRoles(rolesAsString(user.roles()));
        userRepository.save(userEntity);
        return true;
    }

    private String rolesAsString(List<BookStoreUserRole> roles) {
        return roles.stream().map(Enum::name).collect(Collectors.joining(";"));
    }

    private List<BookStoreUserRole> roles(String roles) {
        return Arrays.stream(roles.split(";")).map(BookStoreUserRole::valueOf).toList();
    }
}
