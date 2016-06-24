package me.nbeaussart.payback.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.nbeaussart.payback.domain.ExtandedUser;
import me.nbeaussart.payback.service.ExtandedUserService;
import me.nbeaussart.payback.web.rest.util.HeaderUtil;
import me.nbeaussart.payback.web.rest.util.PaginationUtil;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ExtandedUser.
 */
@RestController
@RequestMapping("/api")
public class ExtandedUserResource {

    private final Logger log = LoggerFactory.getLogger(ExtandedUserResource.class);
        
    @Inject
    private ExtandedUserService extandedUserService;
    
    /**
     * POST  /extanded-users : Create a new extandedUser.
     *
     * @param extandedUser the extandedUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extandedUser, or with status 400 (Bad Request) if the extandedUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/extanded-users",
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
     * PUT  /extanded-users : Updates an existing extandedUser.
     *
     * @param extandedUser the extandedUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated extandedUser,
     * or with status 400 (Bad Request) if the extandedUser is not valid,
     * or with status 500 (Internal Server Error) if the extandedUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/extanded-users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtandedUser> updateExtandedUser(@Valid @RequestBody ExtandedUser extandedUser) throws URISyntaxException {
        log.debug("REST request to update ExtandedUser : {}", extandedUser);
        if (extandedUser.getId() == null) {
            return createExtandedUser(extandedUser);
        }
        ExtandedUser result = extandedUserService.save(extandedUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("extandedUser", extandedUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /extanded-users : get all the extandedUsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of extandedUsers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/extanded-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ExtandedUser>> getAllExtandedUsers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ExtandedUsers");
        Page<ExtandedUser> page = extandedUserService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/extanded-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /extanded-users/:id : get the "id" extandedUser.
     *
     * @param id the id of the extandedUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extandedUser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/extanded-users/{id}",
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
     * DELETE  /extanded-users/:id : delete the "id" extandedUser.
     *
     * @param id the id of the extandedUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/extanded-users/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExtandedUser(@PathVariable Long id) {
        log.debug("REST request to delete ExtandedUser : {}", id);
        extandedUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("extandedUser", id.toString())).build();
    }

}
