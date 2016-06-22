package me.nbeaussart.payback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "longitude")
    private Long longitude;

    @Column(name = "latitude")
    private Long latitude;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_participants",
               joinColumns = @JoinColumn(name="events_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="participants_id", referencedColumnName="ID"))
    private Set<ExtandedUser> participants = new HashSet<>();

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InitialPayment> initialPayiments = new HashSet<>();

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PayBack> paybacks = new HashSet<>();

    @ManyToOne
    private ExtandedUser owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Set<ExtandedUser> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<ExtandedUser> extandedUsers) {
        this.participants = extandedUsers;
    }

    public Set<InitialPayment> getInitialPayiments() {
        return initialPayiments;
    }

    public void setInitialPayiments(Set<InitialPayment> initialPayments) {
        this.initialPayiments = initialPayments;
    }

    public Set<PayBack> getPaybacks() {
        return paybacks;
    }

    public void setPaybacks(Set<PayBack> payBacks) {
        this.paybacks = payBacks;
    }

    public ExtandedUser getOwner() {
        return owner;
    }

    public void setOwner(ExtandedUser extandedUser) {
        this.owner = extandedUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if(event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", name='" + name + "'" +
            ", location='" + location + "'" +
            ", longitude='" + longitude + "'" +
            ", latitude='" + latitude + "'" +
            '}';
    }
}
