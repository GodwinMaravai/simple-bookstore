package be.kata.persistence.user;

import be.kata.persistence.cart.CartEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "T_USER")
public class UserEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NRN")
    private String nrn;

    @Column(name = "ROLE")
    private String roles;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private CartEntity cart;
}
