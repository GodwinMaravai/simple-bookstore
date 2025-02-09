package be.kata.config;

import be.kata.security.BookStoreUserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public static PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(new AntPathRequestMatcher("/register", "POST"))
                                .permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/h2-console")).hasRole(BookStoreUserRole.ADMIN.name())
                                .requestMatchers(new AntPathRequestMatcher("/user", "GET")).hasRole(BookStoreUserRole.ADMIN.name())
                                .requestMatchers(new AntPathRequestMatcher("/books", "POST")).hasRole(BookStoreUserRole.ADMIN.name())
                                .requestMatchers(new AntPathRequestMatcher("/order", "POST")).hasRole(BookStoreUserRole.ADMIN.name())
                                .requestMatchers(new AntPathRequestMatcher("/order", "PUT")).hasRole(BookStoreUserRole.ADMIN.name())
                                .anyRequest()
                                .authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}