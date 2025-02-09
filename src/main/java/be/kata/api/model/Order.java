package be.kata.api.model;

import be.kata.persistence.order.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Valid
public record Order(
        @NotNull long orderId,
        @NotNull long userId,
        @NotNull OrderStatus status,
        @NotNull int totalPrice,
        @NotNull int totalItem,
        @Valid @NotNull List<@NotNull Book> orderedBooks) {
}
