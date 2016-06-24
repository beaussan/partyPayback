package me.nbeaussart.payback.repository;

import me.nbeaussart.payback.domain.Event;
import org.javers.spring.annotation.JaversSpringDataAuditable;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
@JaversSpringDataAuditable
public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("select distinct event from Event event left join fetch event.participants")
    List<Event> findAllWithEagerRelationships();

    @Query("select event from Event event left join fetch event.participants where event.id =:id")
    Event findOneWithEagerRelationships(@Param("id") Long id);

}
