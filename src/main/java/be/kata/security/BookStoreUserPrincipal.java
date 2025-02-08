package be.kata.security;

import be.kata.api.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookStoreUserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final User user;

    public BookStoreUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.name();
    }

    @Override
    public String getPassword() {
        return user.password();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.role().name()));
        return authorities;
    }

    public User getUser() {
        return user;
    }

}
