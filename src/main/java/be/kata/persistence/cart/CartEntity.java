package be.kata.persistence.cart;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "T_CART")
@IdClass(CartId.class)
public class CartEntity {

    @Id
    @Column(name = "USER_ID")
    private long userId;

    @Id
    @Column(name = "BOOK_ID")
    private String bookId;

    @Column(name = "COUNT")
    private int count;

    @Column(name = "PRICE")
    private int price;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
