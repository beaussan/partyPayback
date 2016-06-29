package me.nbeaussart.payback.web.rest.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Event entity.
 */
public class EventDTO implements Serializable {

    private Long id;

    private ZonedDateTime date;

    @NotNull
    private String name;

    private String location;

    private Boolean sendinEmail;


    private Set<ExtandedUserDTO> participants = new HashSet<>();

    private Long ownerId;
    
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
    public Boolean getSendinEmail() {
        return sendinEmail;
    }

    public void setSendinEmail(Boolean sendinEmail) {
        this.sendinEmail = sendinEmail;
    }

    public Set<ExtandedUserDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<ExtandedUserDTO> extandedUsers) {
        this.participants = extandedUsers;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long extandedUserId) {
        this.ownerId = extandedUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;

        if ( ! Objects.equals(id, eventDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", name='" + name + "'" +
            ", location='" + location + "'" +
            ", sendinEmail='" + sendinEmail + "'" +
            '}';
    }
}
