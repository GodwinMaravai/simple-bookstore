package be.kata.persistence.order;

import be.kata.persistence.cart.CartEntity;
import be.kata.persistence.cart.CartItemEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "T_ORDER")
public class OrderEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "STATUS")
    private OrderStatus status;

    @Column(name = "TOTAL_PRICE")
    private int totalPrice;

    @Column(name = "TOTAL_ITEM")
    private int totalItem;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    private CartEntity cart;
}
