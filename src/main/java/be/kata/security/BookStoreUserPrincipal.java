package be.kata.security;

import be.kata.api.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record BookStoreUserPrincipal(User user) implements UserDetails {

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
        return user.roles().stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
    }

}
