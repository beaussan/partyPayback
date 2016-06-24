package me.nbeaussart.payback.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.nbeaussart.payback.domain.InitialPayment;
import me.nbeaussart.payback.service.InitialPaymentService;
import me.nbeaussart.payback.web.rest.util.HeaderUtil;
import me.nbeaussart.payback.web.rest.util.PaginationUtil;
import me.nbeaussart.payback.web.rest.dto.InitialPaymentDTO;
import me.nbeaussart.payback.web.rest.mapper.InitialPaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing InitialPayment.
 */
@RestController
@RequestMapping("/api")
public class InitialPaymentResource {

    private final Logger log = LoggerFactory.getLogger(InitialPaymentResource.class);

    @Inject
    private InitialPaymentService initialPaymentService;

    @Inject
    private InitialPaymentMapper initialPaymentMapper;

    /**
     * POST  /initial-payments : Create a new initialPayment.
     *
     * @param initialPaymentDTO the initialPaymentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new initialPaymentDTO, or with status 400 (Bad Request) if the initialPayment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/initial-payments",
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
     * PUT  /initial-payments : Updates an existing initialPayment.
     *
     * @param initialPaymentDTO the initialPaymentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated initialPaymentDTO,
     * or with status 400 (Bad Request) if the initialPaymentDTO is not valid,
     * or with status 500 (Internal Server Error) if the initialPaymentDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/initial-payments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InitialPaymentDTO> updateInitialPayment(@RequestBody InitialPaymentDTO initialPaymentDTO) throws URISyntaxException {
        log.debug("REST request to update InitialPayment : {}", initialPaymentDTO);
        if (initialPaymentDTO.getId() == null) {
            return createInitialPayment(initialPaymentDTO);
        }
        InitialPaymentDTO result = initialPaymentService.save(initialPaymentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("initialPayment", initialPaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /initial-payments : get all the initialPayments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of initialPayments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/initial-payments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InitialPaymentDTO>> getAllInitialPayments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InitialPayments");
        Page<InitialPayment> page = initialPaymentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/initial-payments");
        return new ResponseEntity<>(initialPaymentMapper.initialPaymentsToInitialPaymentDTOs(page.getContent()), headers, HttpStatus.OK);
    }


    /**
     * GET  /initial-payments : get all the initialPayments.
     *
     * @param id the id of the Evenemnet to retrieve
     * @return the ResponseEntity with status 200 (OK) and the list of initialPayments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/initial-payments/event/{id}",
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
     * GET  /initial-payments/:id : get the "id" initialPayment.
     *
     * @param id the id of the initialPaymentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the initialPaymentDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/initial-payments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InitialPaymentDTO> getInitialPayment(@PathVariable Long id) {
        log.debug("REST request to get InitialPayment : {}", id);
        InitialPaymentDTO initialPaymentDTO = initialPaymentService.findOne(id);
        return Optional.ofNullable(initialPaymentDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /initial-payments/:id : delete the "id" initialPayment.
     *
     * @param id the id of the initialPaymentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/initial-payments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInitialPayment(@PathVariable Long id) {
        log.debug("REST request to delete InitialPayment : {}", id);
        initialPaymentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("initialPayment", id.toString())).build();
    }

}
