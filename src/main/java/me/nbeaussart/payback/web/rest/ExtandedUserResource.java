package me.nbeaussart.payback.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.nbeaussart.payback.domain.ExtandedUser;
import me.nbeaussart.payback.service.ExtandedUserService;
import me.nbeaussart.payback.web.rest.util.HeaderUtil;
import me.nbeaussart.payback.web.rest.util.PaginationUtil;
import me.nbeaussart.payback.web.rest.dto.ExtandedUserDTO;
import me.nbeaussart.payback.web.rest.mapper.ExtandedUserMapper;
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
 * REST controller for managing ExtandedUser.
 */
@RestController
@RequestMapping("/api")
public class ExtandedUserResource {

    private final Logger log = LoggerFactory.getLogger(ExtandedUserResource.class);
        
    @Inject
    private ExtandedUserService extandedUserService;
    
    @Inject
    private ExtandedUserMapper extandedUserMapper;
    
    /**
     * POST  /extanded-users : Create a new extandedUser.
     *
     * @param extandedUserDTO the extandedUserDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extandedUserDTO, or with status 400 (Bad Request) if the extandedUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/extanded-users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtandedUserDTO> createExtandedUser(@RequestBody ExtandedUserDTO extandedUserDTO) throws URISyntaxException {
        log.debug("REST request to save ExtandedUser : {}", extandedUserDTO);
        if (extandedUserDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("extandedUser", "idexists", "A new extandedUser cannot already have an ID")).body(null);
        }
        ExtandedUserDTO result = extandedUserService.save(extandedUserDTO);
        return ResponseEntity.created(new URI("/api/extanded-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("extandedUser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /extanded-users : Updates an existing extandedUser.
     *
     * @param extandedUserDTO the extandedUserDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated extandedUserDTO,
     * or with status 400 (Bad Request) if the extandedUserDTO is not valid,
     * or with status 500 (Internal Server Error) if the extandedUserDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/extanded-users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtandedUserDTO> updateExtandedUser(@RequestBody ExtandedUserDTO extandedUserDTO) throws URISyntaxException {
        log.debug("REST request to update ExtandedUser : {}", extandedUserDTO);
        if (extandedUserDTO.getId() == null) {
            return createExtandedUser(extandedUserDTO);
        }
        ExtandedUserDTO result = extandedUserService.save(extandedUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("extandedUser", extandedUserDTO.getId().toString()))
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
    public ResponseEntity<List<ExtandedUserDTO>> getAllExtandedUsers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ExtandedUsers");
        Page<ExtandedUser> page = extandedUserService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/extanded-users");
        return new ResponseEntity<>(extandedUserMapper.extandedUsersToExtandedUserDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /extanded-users/:id : get the "id" extandedUser.
     *
     * @param id the id of the extandedUserDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extandedUserDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/extanded-users/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtandedUserDTO> getExtandedUser(@PathVariable Long id) {
        log.debug("REST request to get ExtandedUser : {}", id);
        ExtandedUserDTO extandedUserDTO = extandedUserService.findOne(id);
        return Optional.ofNullable(extandedUserDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /extanded-users/:id : delete the "id" extandedUser.
     *
     * @param id the id of the extandedUserDTO to delete
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
