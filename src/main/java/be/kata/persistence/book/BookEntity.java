package be.kata.persistence.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.lang.NonNull;

import java.util.Objects;

@Entity
@Table(name = "T_BOOK")
public class BookEntity {

    @Id
    @NonNull
    @Column(name = "ID")
    private String id;

    @NonNull
    @Column(name = "NAME")
    private String name;

    @NonNull
    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "COUNT")
    private int count;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    public void setAuthor(@NonNull String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookEntity that)) return false;
        return price == that.price && count == that.count && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, price, count);
    }
}
