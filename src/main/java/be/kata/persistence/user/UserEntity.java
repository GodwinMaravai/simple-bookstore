package be.kata.persistence.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "T_USER")
public class UserEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserIdGenerator")
    @SequenceGenerator(name = "UserIdGenerator", sequenceName = "T_USER_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NRN")
    private String nrn;

    @Column(name = "GSM")
    private String gsm;

    @Column(name = "CONSENT_STATUS")
    private boolean isConsent;

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

    public String getNrn() {
        return nrn;
    }

    public void setNrn(String nrn) {
        this.nrn = nrn;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public boolean isConsent() {
        return isConsent;
    }

    public void setConsent(boolean consent) {
        isConsent = consent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return id == that.id && isConsent == that.isConsent && Objects.equals(name, that.name) && Objects.equals(nrn, that.nrn) && Objects.equals(gsm, that.gsm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nrn, gsm, isConsent);
    }
}
