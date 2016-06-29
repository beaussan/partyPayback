package me.nbeaussart.payback.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ExtandedUser entity.
 */
public class ExtandedUserDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String email;

    private Boolean sendinEmail;


    private Long userId;
    
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
    public Boolean getSendinEmail() {
        return sendinEmail;
    }

    public void setSendinEmail(Boolean sendinEmail) {
        this.sendinEmail = sendinEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExtandedUserDTO extandedUserDTO = (ExtandedUserDTO) o;

        if ( ! Objects.equals(id, extandedUserDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExtandedUserDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", email='" + email + "'" +
            ", sendinEmail='" + sendinEmail + "'" +
            '}';
    }
}
