package me.nbeaussart.payback.repository;

import me.nbeaussart.payback.domain.PayBack;
import org.javers.spring.annotation.JaversSpringDataAuditable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PayBack entity.
 */
@SuppressWarnings("unused")
@JaversSpringDataAuditable
public interface PayBackRepository extends JpaRepository<PayBack,Long> {

}
