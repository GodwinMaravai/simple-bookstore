package be.kata.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartItem(
        @NotBlank String bookId,
        @NotNull int count) {
}
