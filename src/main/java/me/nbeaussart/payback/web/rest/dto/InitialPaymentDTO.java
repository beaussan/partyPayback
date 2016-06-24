package me.nbeaussart.payback.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the InitialPayment entity.
 */
public class InitialPaymentDTO implements Serializable {

    private Long id;

    private Double ammount;


    private Long userId;
    
    private Long eventId;
    
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long extandedUserId) {
        this.userId = extandedUserId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InitialPaymentDTO initialPaymentDTO = (InitialPaymentDTO) o;

        if ( ! Objects.equals(id, initialPaymentDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InitialPaymentDTO{" +
            "id=" + id +
            ", ammount='" + ammount + "'" +
            '}';
    }
}
