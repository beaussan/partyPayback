package me.nbeaussart.payback.service;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.nbeaussart.payback.domain.Event;
import me.nbeaussart.payback.domain.ExtandedUser;
import me.nbeaussart.payback.domain.InitialPayment;
import me.nbeaussart.payback.domain.PayBack;
import me.nbeaussart.payback.repository.EventRepository;
import me.nbeaussart.payback.service.util.PayBackProcessHelper;
import me.nbeaussart.payback.web.rest.dto.EventDTO;
import me.nbeaussart.payback.web.rest.mapper.EventMapper;

/**
 * Service Implementation for managing Event.
 */
@Service
@Transactional
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);

    @Inject
    private EventRepository eventRepository;

    @Inject
    private EventMapper eventMapper;


    @Inject
    private PayBackService payBackService;

    /**
     * Save a event.
     *
     * @param eventDTO the entity to save
     * @return the persisted entity
     */
    public EventDTO save(EventDTO eventDTO) {
        log.debug("Request to save Event : {}", eventDTO);
        Event event = eventMapper.eventDTOToEvent(eventDTO);
        event = eventRepository.save(event);
        EventDTO result = eventMapper.eventToEventDTO(event);
        return result;
    }

    /**
     *  Get all the events.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Event> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        Page<Event> result = eventRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one event by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EventDTO findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        Event event = eventRepository.findOneWithEagerRelationships(id);
        EventDTO eventDTO = eventMapper.eventToEventDTO(event);
        return eventDTO;
    }

    /**
     *  Delete the  event by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.delete(id);
    }

    /**
     * Build all the paybacks for the event
     * @param id the id of the entity
     * @return the entity build
     */
    public EventDTO buildById(Long id) {
        log.debug("Request to build event : {}", id);
        Event event = eventRepository.findOneWithEagerRelationships(id);

        Set<InitialPayment> initialPayments = event.getInitialPayiments();
        Set<PayBack> paybacks = event.getPaybacks();
        Set<ExtandedUser> participants = event.getParticipants();
        
        PayBackProcessHelper processHelper = new PayBackProcessHelper();
        Double ammountPerUser = processHelper.getTotalAmmountPerPerson(initialPayments, participants);
        Map<ExtandedUser, Double> participantPayments = processHelper.getAllPaymentPerPerson(initialPayments);

        Map<ExtandedUser, Double> debtors = new HashMap<ExtandedUser, Double>();
        Map<ExtandedUser, Double> creditors = new HashMap<ExtandedUser, Double>();
        processHelper.getDebtorsAndCreditors(participantPayments, ammountPerUser, debtors, creditors);

        Set<PayBack> newPayBacks = processHelper.getNewPayBacks(event, paybacks, debtors, creditors, this.payBackService);

        event.setPaybacks(newPayBacks);
        
        EventDTO eventDTO = eventMapper.eventToEventDTO(event);
        return eventDTO;
    }
}
