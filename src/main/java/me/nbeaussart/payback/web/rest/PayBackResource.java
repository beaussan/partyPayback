package me.nbeaussart.payback.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.nbeaussart.payback.domain.PayBack;
import me.nbeaussart.payback.service.PayBackService;
import me.nbeaussart.payback.web.rest.util.HeaderUtil;
import me.nbeaussart.payback.web.rest.util.PaginationUtil;
import me.nbeaussart.payback.web.rest.dto.PayBackDTO;
import me.nbeaussart.payback.web.rest.mapper.PayBackMapper;
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
 * REST controller for managing PayBack.
 */
@RestController
@RequestMapping("/api")
public class PayBackResource {

    private final Logger log = LoggerFactory.getLogger(PayBackResource.class);
        
    @Inject
    private PayBackService payBackService;
    
    @Inject
    private PayBackMapper payBackMapper;
    
    /**
     * POST  /pay-backs : Create a new payBack.
     *
     * @param payBackDTO the payBackDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new payBackDTO, or with status 400 (Bad Request) if the payBack has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pay-backs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PayBackDTO> createPayBack(@RequestBody PayBackDTO payBackDTO) throws URISyntaxException {
        log.debug("REST request to save PayBack : {}", payBackDTO);
        if (payBackDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("payBack", "idexists", "A new payBack cannot already have an ID")).body(null);
        }
        PayBackDTO result = payBackService.save(payBackDTO);
        return ResponseEntity.created(new URI("/api/pay-backs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("payBack", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pay-backs : Updates an existing payBack.
     *
     * @param payBackDTO the payBackDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated payBackDTO,
     * or with status 400 (Bad Request) if the payBackDTO is not valid,
     * or with status 500 (Internal Server Error) if the payBackDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pay-backs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PayBackDTO> updatePayBack(@RequestBody PayBackDTO payBackDTO) throws URISyntaxException {
        log.debug("REST request to update PayBack : {}", payBackDTO);
        if (payBackDTO.getId() == null) {
            return createPayBack(payBackDTO);
        }
        PayBackDTO result = payBackService.save(payBackDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("payBack", payBackDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pay-backs : get all the payBacks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of payBacks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/pay-backs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PayBackDTO>> getAllPayBacks(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PayBacks");
        Page<PayBack> page = payBackService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pay-backs");
        return new ResponseEntity<>(payBackMapper.payBacksToPayBackDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pay-backs/:id : get the "id" payBack.
     *
     * @param id the id of the payBackDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the payBackDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pay-backs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PayBackDTO> getPayBack(@PathVariable Long id) {
        log.debug("REST request to get PayBack : {}", id);
        PayBackDTO payBackDTO = payBackService.findOne(id);
        return Optional.ofNullable(payBackDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pay-backs/:id : delete the "id" payBack.
     *
     * @param id the id of the payBackDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pay-backs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePayBack(@PathVariable Long id) {
        log.debug("REST request to delete PayBack : {}", id);
        payBackService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("payBack", id.toString())).build();
    }

}
