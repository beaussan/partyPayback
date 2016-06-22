package me.nbeaussart.payback.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import me.nbeaussart.payback.domain.enumeration.Genders;

/**
 * A DTO for the ExtandedUser entity.
 */
public class ExtandedUserDTO implements Serializable {

    private Long id;

    private String description;

    private Integer age;

    private Genders gender;


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
            ", description='" + description + "'" +
            ", age='" + age + "'" +
            ", gender='" + gender + "'" +
            '}';
    }
}
