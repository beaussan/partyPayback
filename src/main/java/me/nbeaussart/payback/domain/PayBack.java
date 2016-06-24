package me.nbeaussart.payback.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PayBack.
 */
@Entity
@Table(name = "pay_back")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PayBack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ammount")
    private Double ammount;

    @Column(name = "timestamp")
    private ZonedDateTime timestamp;

    @ManyToOne
    private ExtandedUser source;

    @ManyToOne
    private ExtandedUser toPay;

    @ManyToOne
    private Event event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmmount() {
        return ammount;
    }

    public void setAmmount(Double ammount) {
        this.ammount = ammount;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ExtandedUser getSource() {
        return source;
    }

    public void setSource(ExtandedUser extandedUser) {
        this.source = extandedUser;
    }

    public ExtandedUser getToPay() {
        return toPay;
    }

    public void setToPay(ExtandedUser extandedUser) {
        this.toPay = extandedUser;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PayBack payBack = (PayBack) o;
        if(payBack.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, payBack.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PayBack{" +
            "id=" + id +
            ", ammount='" + ammount + "'" +
            ", timestamp='" + timestamp + "'" +
            '}';
    }
}
