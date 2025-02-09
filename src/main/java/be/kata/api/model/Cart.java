package be.kata.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record Cart(
        @NotNull long userId,
        @Valid @NotNull List<@NotNull CartItem> bookCounts) {
}
