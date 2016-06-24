package me.nbeaussart.payback.repository;

import me.nbeaussart.payback.domain.PayBack;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PayBack entity.
 */
@SuppressWarnings("unused")
public interface PayBackRepository extends JpaRepository<PayBack,Long> {

    List<PayBack> findByEventId(Long id);
}
