package be.kata.api.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Book(@NotBlank String id,
                   @NotBlank String title,
                   @NotBlank String author,
                   @NotNull @Min(1) Integer price,
                   @NotNull @Min(1) Integer count) {
}
