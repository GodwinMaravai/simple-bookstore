package be.kata.api.model;

import be.kata.security.BookStoreUserRole;

import java.util.List;

public record DisplayUser(long id, String name, List<BookStoreUserRole> roles) {
}
