package be.kata.api.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Valid
public record User(
        long id,
        @Max(100) @NotEmpty String name,
        @NotEmpty @Pattern(regexp = "^\\d{11}$") @Size(min = 11, max = 11) String nrn,
        @NotEmpty @Pattern(regexp = "^\\d{10}$") @Size(min = 10, max = 10) String gsm) {
}
