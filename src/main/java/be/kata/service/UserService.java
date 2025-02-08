package be.kata.service;

import be.kata.api.model.User;
import be.kata.persistence.user.UserEntity;
import be.kata.persistence.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserExist(String name) {
        return !isEmpty(userRepository.findUserEntityByName(name));
    }

    public boolean createUser(User user) {
        Assert.notNull(user, "Invalid user");
        UserEntity userEntity = new UserEntity();
        userEntity.setNrn(user.nrn());
        userEntity.setName(user.name());
        userEntity.setGsm(user.gsm());
        userRepository.save(userEntity);
        return true;
    }
}
