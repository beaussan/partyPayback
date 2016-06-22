package me.nbeaussart.payback.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the PayBack entity.
 */
public class PayBackDTO implements Serializable {

    private Long id;

    private Long ammount;

    private ZonedDateTime timestamp;


    private Long sourceId;
    
    private Long toPayId;
    
    private Long eventId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getAmmount() {
        return ammount;
    }

    public void setAmmount(Long ammount) {
        this.ammount = ammount;
    }
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long extandedUserId) {
        this.sourceId = extandedUserId;
    }

    public Long getToPayId() {
        return toPayId;
    }

    public void setToPayId(Long extandedUserId) {
        this.toPayId = extandedUserId;
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

        PayBackDTO payBackDTO = (PayBackDTO) o;

        if ( ! Objects.equals(id, payBackDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PayBackDTO{" +
            "id=" + id +
            ", ammount='" + ammount + "'" +
            ", timestamp='" + timestamp + "'" +
            '}';
    }
}
