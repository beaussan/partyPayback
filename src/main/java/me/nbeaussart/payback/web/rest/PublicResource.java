package me.nbeaussart.payback.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.nbeaussart.payback.domain.ExtandedUser;
import me.nbeaussart.payback.domain.InitialPayment;
import me.nbeaussart.payback.domain.PayBack;
import me.nbeaussart.payback.service.EventService;
import me.nbeaussart.payback.service.ExtandedUserService;
import me.nbeaussart.payback.service.InitialPaymentService;
import me.nbeaussart.payback.service.PayBackService;
import me.nbeaussart.payback.web.rest.dto.EventDTO;
import me.nbeaussart.payback.web.rest.dto.InitialPaymentDTO;
import me.nbeaussart.payback.web.rest.dto.PayBackDTO;
import me.nbeaussart.payback.web.rest.mapper.EventMapper;
import me.nbeaussart.payback.web.rest.mapper.InitialPaymentMapper;
import me.nbeaussart.payback.web.rest.mapper.PayBackMapper;
import me.nbeaussart.payback.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing public calls.
 */
@RestController
@RequestMapping("/api")
public class PublicResource {

    private final Logger log = LoggerFactory.getLogger(PublicResource.class);

    @Inject
    private PayBackService payBackService;

    @Inject
    private PayBackMapper payBackMapper;

    @Inject
    private ExtandedUserService extandedUserService;

    @Inject
    private InitialPaymentService initialPaymentService;

    @Inject
    private InitialPaymentMapper initialPaymentMapper;

    @Inject
    private EventService eventService;

    @Inject
    private EventMapper eventMapper;


    /**
     * GET  /initial-payments : get all the payBacks.
     *
     * @param id the id of the Evenemnet to retrieve
     * @return the ResponseEntity with status 200 (OK) and the list of payBacks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/public/pay-backs/event/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PayBackDTO>> getAllPayBacksForEvent(@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to get a page of InitialPayments");
        List<PayBack> page = payBackService.findAllByEvent(id);
        return new ResponseEntity<>(payBackMapper.payBacksToPayBackDTOs(page), HttpStatus.OK);
    }



    /**
     * POST  /initial-payments : Create a new initialPayment.
     *
     * @param initialPaymentDTO the initialPaymentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new initialPaymentDTO, or with status 400 (Bad Request) if the initialPayment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/public/initial-payments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InitialPaymentDTO> createInitialPayment(@RequestBody InitialPaymentDTO initialPaymentDTO) throws URISyntaxException {
        log.debug("REST request to save InitialPayment : {}", initialPaymentDTO);
        if (initialPaymentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("initialPayment", "idexists", "A new initialPayment cannot already have an ID")).body(null);
        }
        InitialPaymentDTO result = initialPaymentService.save(initialPaymentDTO);
        return ResponseEntity.created(new URI("/api/initial-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("initialPayment", result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /initial-payments : get all the initialPayments.
     *
     * @param id the id of the Evenemnet to retrieve
     * @return the ResponseEntity with status 200 (OK) and the list of initialPayments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/public/initial-payments/event/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InitialPaymentDTO>> getAllInitialPaymentsForEvent(@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to get a page of InitialPayments");
        List<InitialPayment> page = initialPaymentService.findAllByEvent(id);
        return new ResponseEntity<>(initialPaymentMapper.initialPaymentsToInitialPaymentDTOs(page), HttpStatus.OK);
    }




    /**
     * POST  /extanded-users : Create a new extandedUser.
     *
     * @param extandedUser the extandedUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extandedUser, or with status 400 (Bad Request) if the extandedUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/public/extanded-users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtandedUser> createExtandedUser(@Valid @RequestBody ExtandedUser extandedUser) throws URISyntaxException {
        log.debug("REST request to save ExtandedUser : {}", extandedUser);
        if (extandedUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("extandedUser", "idexists", "A new extandedUser cannot already have an ID")).body(null);
        }
        ExtandedUser result = extandedUserService.save(extandedUser);
        return ResponseEntity.created(new URI("/api/extanded-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("extandedUser", result.getId().toString()))
            .body(result);
    }



    /**
     * GET  /extanded-users/:id : get the "id" extandedUser.
     *
     * @param id the id of the extandedUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extandedUser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/public/extanded-users/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtandedUser> getExtandedUser(@PathVariable Long id) {
        log.debug("REST request to get ExtandedUser : {}", id);
        ExtandedUser extandedUser = extandedUserService.findOne(id);
        return Optional.ofNullable(extandedUser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * POST  /events : Create a new event.
     *
     * @param eventDTO the eventDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new eventDTO, or with status 400 (Bad Request) if the event has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/public/events",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) throws URISyntaxException {
        log.debug("REST request to save Event : {}", eventDTO);
        if (eventDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("event", "idexists", "A new event cannot already have an ID")).body(null);
        }
        EventDTO result = eventService.save(eventDTO);
        return ResponseEntity.created(new URI("/api/events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("event", result.getId().toString()))
            .body(result);
    }


    /**
     * GET  /events/:id : get the "id" event.
     *
     * @param id the id of the eventDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eventDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/public/events/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long id) {
        log.debug("REST request to get Event : {}", id);
        EventDTO eventDTO = eventService.findOne(id);
        return Optional.ofNullable(eventDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * GET  /events/:id : get the "id" event.
     *
     * @param id the id of the eventDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eventDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/public/events/{id}/build",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventDTO> buildEvent(@PathVariable Long id) {
        log.debug("REST request to get Event : {}", id);
        EventDTO eventDTO = eventService.buildById(id);
        return Optional.ofNullable(eventDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }





}
