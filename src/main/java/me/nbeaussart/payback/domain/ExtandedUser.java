package me.nbeaussart.payback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import me.nbeaussart.payback.domain.enumeration.Genders;

/**
 * A ExtandedUser.
 */
@Entity
@Table(name = "extanded_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExtandedUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Genders gender;

    @OneToMany(mappedBy = "source")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PayBack> toPays = new HashSet<>();

    @OneToMany(mappedBy = "toPay")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PayBack> payRecives = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InitialPayment> initialPaiments = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> eventParcipatings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Genders getGender() {
        return gender;
    }

    public void setGender(Genders gender) {
        this.gender = gender;
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
            ", description='" + description + "'" +
            ", age='" + age + "'" +
            ", gender='" + gender + "'" +
            '}';
    }
}
