package be.kata.persistence.cart;

import java.io.Serializable;
import java.util.Objects;

public class CartId implements Serializable {

    private long userId;
    private String bookId;

    public CartId() {
    }

    public CartId(long userId, String bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartId cartId)) return false;
        return userId == cartId.userId && Objects.equals(bookId, cartId.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }
}
