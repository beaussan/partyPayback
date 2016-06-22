package me.nbeaussart.payback.repository;

import me.nbeaussart.payback.domain.ExtandedUser;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ExtandedUser entity.
 */
@SuppressWarnings("unused")
public interface ExtandedUserRepository extends JpaRepository<ExtandedUser,Long> {

}
