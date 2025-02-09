package be.kata.api.model;

import be.kata.persistence.order.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UserOrderStatus(
        @NotNull long userId,
        @NotNull long orderId,
        @NotNull OrderStatus status) {
}
