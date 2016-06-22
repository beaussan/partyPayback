package me.nbeaussart.payback.service;

import me.nbeaussart.payback.domain.PayBack;
import me.nbeaussart.payback.repository.PayBackRepository;
import me.nbeaussart.payback.web.rest.dto.PayBackDTO;
import me.nbeaussart.payback.web.rest.mapper.PayBackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PayBack.
 */
@Service
@Transactional
public class PayBackService {

    private final Logger log = LoggerFactory.getLogger(PayBackService.class);
    
    @Inject
    private PayBackRepository payBackRepository;
    
    @Inject
    private PayBackMapper payBackMapper;
    
    /**
     * Save a payBack.
     * 
     * @param payBackDTO the entity to save
     * @return the persisted entity
     */
    public PayBackDTO save(PayBackDTO payBackDTO) {
        log.debug("Request to save PayBack : {}", payBackDTO);
        PayBack payBack = payBackMapper.payBackDTOToPayBack(payBackDTO);
        payBack = payBackRepository.save(payBack);
        PayBackDTO result = payBackMapper.payBackToPayBackDTO(payBack);
        return result;
    }

    /**
     *  Get all the payBacks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PayBack> findAll(Pageable pageable) {
        log.debug("Request to get all PayBacks");
        Page<PayBack> result = payBackRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one payBack by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PayBackDTO findOne(Long id) {
        log.debug("Request to get PayBack : {}", id);
        PayBack payBack = payBackRepository.findOne(id);
        PayBackDTO payBackDTO = payBackMapper.payBackToPayBackDTO(payBack);
        return payBackDTO;
    }

    /**
     *  Delete the  payBack by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PayBack : {}", id);
        payBackRepository.delete(id);
    }
}
