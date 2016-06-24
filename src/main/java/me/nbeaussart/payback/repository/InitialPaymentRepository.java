package me.nbeaussart.payback.repository;

import me.nbeaussart.payback.domain.InitialPayment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InitialPayment entity.
 */
@SuppressWarnings("unused")
public interface InitialPaymentRepository extends JpaRepository<InitialPayment,Long> {

    List<InitialPayment> findByEventId(Long id);
}
