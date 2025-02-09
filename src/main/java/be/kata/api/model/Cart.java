package be.kata.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record Cart(
        @NotBlank String name,
        @Valid @NotNull List<@NotNull CartItem> bookCounts) {
}
