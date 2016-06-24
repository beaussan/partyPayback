package me.nbeaussart.payback.repository;

import me.nbeaussart.payback.domain.InitialPayment;
import org.javers.spring.annotation.JaversSpringDataAuditable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InitialPayment entity.
 */
@SuppressWarnings("unused")
@JaversSpringDataAuditable
public interface InitialPaymentRepository extends JpaRepository<InitialPayment,Long> {

}
