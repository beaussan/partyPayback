package me.nbeaussart.payback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ExtandedUser.
 */
@Entity
@Table(name = "extanded_user")
public class ExtandedUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "sendin_email")
    private Boolean sendinEmail;

    @OneToMany(mappedBy = "source")
    @JsonIgnore
    private Set<PayBack> toPays = new HashSet<>();

    @OneToMany(mappedBy = "toPay")
    @JsonIgnore
    private Set<PayBack> payRecives = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<InitialPayment> initialPaiments = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    @JsonIgnore
    private Set<Event> eventParcipatings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isSendinEmail() {
        return sendinEmail;
    }

    public void setSendinEmail(Boolean sendinEmail) {
        this.sendinEmail = sendinEmail;
    }

    public Set<PayBack> getToPays() {
        return toPays;
    }

    public void setToPays(Set<PayBack> payBacks) {
        this.toPays = payBacks;
    }

    public Set<PayBack> getPayRecives() {
        return payRecives;
    }

    public void setPayRecives(Set<PayBack> payBacks) {
        this.payRecives = payBacks;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<InitialPayment> getInitialPaiments() {
        return initialPaiments;
    }

    public void setInitialPaiments(Set<InitialPayment> initialPayments) {
        this.initialPaiments = initialPayments;
    }

    public Set<Event> getEventParcipatings() {
        return eventParcipatings;
    }

    public void setEventParcipatings(Set<Event> events) {
        this.eventParcipatings = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtandedUser extandedUser = (ExtandedUser) o;
        if(extandedUser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, extandedUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExtandedUser{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", email='" + email + "'" +
            ", sendinEmail='" + sendinEmail + "'" +
            '}';
    }
}
