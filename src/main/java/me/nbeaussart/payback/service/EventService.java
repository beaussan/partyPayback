package me.nbeaussart.payback.service;

import java.time.ZonedDateTime;
import java.util.HashMap;
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
        Map<ExtandedUser, Double> participantPayment = new HashMap<ExtandedUser, Double>();
        Double totalAmmount = Double.valueOf(0);
        
        for (InitialPayment payment : initialPayments){
        	ExtandedUser user = payment.getUser();
        	Double ammount = payment.getAmmount();
        	totalAmmount += ammount;
        	ammount += participantPayment.containsKey(user) ? participantPayment.get(user) : 0;
        	participantPayment.put(user, ammount);
        }

        Double ammountPerUser = totalAmmount/participants.size();
        Map<ExtandedUser, Double> debtors = new HashMap<ExtandedUser, Double>();
        Map<ExtandedUser, Double> creditors = new HashMap<ExtandedUser, Double>();
        for (Entry<ExtandedUser, Double> entry : participantPayment.entrySet()){
        	Double value = ammountPerUser - entry.getValue();
        	if (value > 0){
        		debtors.put(entry.getKey(), value);
        	} else if (value < 0){
        		creditors.put(entry.getKey(), Math.abs(value));
        	}
        }
        
        for (PayBack payback : paybacks){
        	if (payback.isIsPaid()){
            	ExtandedUser source = payback.getSource();
            	ExtandedUser toPay = payback.getToPay();
            	Double ammount = payback.getAmmount();
            	if (creditors.containsKey(source)){
            		creditors.put(source, creditors.get(source)+ammount);
            	} else {
            		Double newSolde = debtors.get(source)-ammount;
            		if (newSolde < 0){
            			debtors.remove(source);
            			creditors.put(source, Math.abs(newSolde));
            		} else {
            			debtors.put(source, newSolde);
            		}
            	}
            	if (creditors.containsKey(toPay)){
            		Double newSolde = creditors.get(toPay) - ammount;
            		if (newSolde < 0){
            			creditors.remove(toPay);
            			debtors.put(toPay, Math.abs(newSolde));
            		} else {
            			creditors.put(toPay, newSolde);
            		}
            	} else {
            		debtors.put(toPay, debtors.get(toPay) + ammount);
            	}
        	} else {
        		payBackService.delete(payback.getId());
        		paybacks.remove(payback);
        	}
        }
        
        creditors.entrySet().stream().sorted(Map.Entry.<ExtandedUser, Double>comparingByValue());
        debtors.entrySet().stream().sorted(Map.Entry.<ExtandedUser, Double>comparingByValue());
        
        for (Entry<ExtandedUser, Double> creditor : creditors.entrySet()){
        	Double solde = creditor.getValue();
        	while (solde > 0){
        		Entry<ExtandedUser, Double> debtor = debtors.entrySet().iterator().next();
        		solde -= debtor.getValue();
        		if (solde <= 0){
        			debtor.setValue(Math.abs(solde));
        		} else {
        			debtors.remove(debtor.getKey());
        		}
        		PayBack payback = new PayBack();
        		payback.setAmmount(Math.abs(solde));
        		payback.setEvent(event);
        		payback.setIsPaid(false);
        		payback.setSource(creditor.getKey());
        		payback.setToPay(debtor.getKey());
        		payback.setTimestamp(ZonedDateTime.now());
        		paybacks.add(payback);
        		payBackService.save(payback);
        	}
        }
        
        EventDTO eventDTO = eventMapper.eventToEventDTO(event);
        return eventDTO;
    }
}
