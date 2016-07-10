package me.nbeaussart.payback.web.rest.dto;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by beaussan on 09/07/16.
 */
public class EventFullDTO {


    private Long id;

    private ZonedDateTime date;

    @NotNull
    private String name;

    private String location;

    private Boolean sendinEmail;


    private Set<ExtendedUserEventDTO> participants = new HashSet<>();

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

    public Set<ExtendedUserEventDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<ExtendedUserEventDTO> participants) {
        this.participants = participants;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventFullDTO that = (EventFullDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (sendinEmail != null ? !sendinEmail.equals(that.sendinEmail) : that.sendinEmail != null) return false;
        if (participants != null ? !participants.equals(that.participants) : that.participants != null) return false;
        return ownerId != null ? ownerId.equals(that.ownerId) : that.ownerId == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (sendinEmail != null ? sendinEmail.hashCode() : 0);
        result = 31 * result + (participants != null ? participants.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventFullDTO{" +
            "id=" + id +
            ", date=" + date +
            ", name='" + name + '\'' +
            ", location='" + location + '\'' +
            ", sendinEmail=" + sendinEmail +
            ", participants=" + participants +
            ", ownerId=" + ownerId +
            '}';
    }

    public static class ExtendedUserEventDTO {

        @NotNull
        private String name;

        private String email;

        private Double paiment;

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

        public Double getPaiment() {
            return paiment;
        }

        public void setPaiment(Double paiment) {
            this.paiment = paiment;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ExtendedUserEventDTO that = (ExtendedUserEventDTO) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (email != null ? !email.equals(that.email) : that.email != null) return false;
            return paiment != null ? paiment.equals(that.paiment) : that.paiment == null;

        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (email != null ? email.hashCode() : 0);
            result = 31 * result + (paiment != null ? paiment.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ExtendedUserEventDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", paiment=" + paiment +
                '}';
        }
    }


}
