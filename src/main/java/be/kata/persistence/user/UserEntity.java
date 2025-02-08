package be.kata.persistence.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "T_USER")
public class UserEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    private String password;

    @Column(name = "NRN")
    private String nrn;

    @Column(name = "ROLE")
    private UserRole role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNrn() {
        return nrn;
    }

    public void setNrn(String nrn) {
        this.nrn = nrn;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(password, that.password) && Objects.equals(nrn, that.nrn) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, nrn, role);
    }
}
