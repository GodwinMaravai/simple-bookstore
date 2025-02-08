package be.kata.api.model;

import be.kata.persistence.user.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record User(
        @NotBlank String name,
        @NotBlank String password,
        @NotBlank @Pattern(regexp = "^[0-9]{11}$") @Size(min = 11, max = 11) String nrn,
        @NotNull UserRole role) {
}
