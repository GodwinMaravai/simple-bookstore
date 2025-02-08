package be.kata.service;

import be.kata.api.model.User;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    public User findUserByName(String name) {
        UserEntity userEntity = userRepository.findUserEntityByName(name);
        return new User(userEntity.getName(), userEntity.getPassword(), userEntity.getNrn(), userEntity.getRole());
    }

    public boolean createUser(User user) {
        Assert.notNull(user, "Invalid user");
        UserEntity userEntity = new UserEntity();
        userEntity.setNrn(user.nrn());
        userEntity.setName(user.name());
        userEntity.setPassword(passwordEncoder.encode(user.password()));
        userEntity.setRole(user.role());
        userRepository.save(userEntity);
        return true;
    }
}
