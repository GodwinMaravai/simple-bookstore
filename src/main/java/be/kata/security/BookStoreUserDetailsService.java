package be.kata.security;

import be.kata.api.model.User;
import be.kata.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookStoreUserDetailsService implements UserDetailsService {

    private final UserService userService;

    BookStoreUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new BookStoreUserPrincipal(user);
    }
}
