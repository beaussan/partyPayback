package me.nbeaussart.payback.service;

import me.nbeaussart.payback.domain.ExtandedUser;
import me.nbeaussart.payback.repository.ExtandedUserRepository;
import me.nbeaussart.payback.web.rest.dto.ExtandedUserDTO;
import me.nbeaussart.payback.web.rest.mapper.ExtandedUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ExtandedUser.
 */
@Service
@Transactional
public class ExtandedUserService {

    private final Logger log = LoggerFactory.getLogger(ExtandedUserService.class);

    @Inject
    private ExtandedUserRepository extandedUserRepository;

    @Inject
    private ExtandedUserMapper extandedUserMapper;

    /**
     * Save a extandedUser.
     *
     * @param extandedUserDTO the entity to save
     * @return the persisted entity
     */
    public ExtandedUserDTO save(ExtandedUserDTO extandedUserDTO) {
        log.debug("Request to save ExtandedUser : {}", extandedUserDTO);
        ExtandedUser extandedUser = extandedUserMapper.extandedUserDTOToExtandedUser(extandedUserDTO);
        extandedUser = extandedUserRepository.save(extandedUser);
        ExtandedUserDTO result = extandedUserMapper.extandedUserToExtandedUserDTO(extandedUser);
        return result;
    }

    public ExtandedUserDTO createNew(ExtandedUserDTO extandedUserDTO){
        log.debug("Reques to create ExtendedUser : {}", extandedUserDTO);

        ExtandedUser extandedUser = extandedUserRepository.findOneByEmail(extandedUserDTO.getEmail()).orElseGet(() -> {
            ExtandedUser extandedUserToSave = extandedUserMapper.extandedUserDTOToExtandedUser(extandedUserDTO);
            return extandedUserRepository.save(extandedUserToSave);
        });

        ExtandedUserDTO result = extandedUserMapper.extandedUserToExtandedUserDTO(extandedUser);
        return result;


    }

    /**
     *  Get all the extandedUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ExtandedUser> findAll(Pageable pageable) {
        log.debug("Request to get all ExtandedUsers");
        Page<ExtandedUser> result = extandedUserRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one extandedUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ExtandedUserDTO findOne(Long id) {
        log.debug("Request to get ExtandedUser : {}", id);
        ExtandedUser extandedUser = extandedUserRepository.findOne(id);
        ExtandedUserDTO extandedUserDTO = extandedUserMapper.extandedUserToExtandedUserDTO(extandedUser);
        return extandedUserDTO;
    }

    /**
     *  Delete the  extandedUser by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExtandedUser : {}", id);
        extandedUserRepository.delete(id);
    }
}
