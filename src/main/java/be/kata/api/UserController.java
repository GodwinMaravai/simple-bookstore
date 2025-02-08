package be.kata.api;

import be.kata.api.model.User;
import be.kata.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(Authentication authentication) {
        return ResponseEntity.ok("Welcome '%s'".formatted(authentication.getName()));
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        if (userService.isUserExist(user.name())) {
            return ResponseEntity.badRequest().build();
        }
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User '%s' created".formatted(user.name()));
    }
}
