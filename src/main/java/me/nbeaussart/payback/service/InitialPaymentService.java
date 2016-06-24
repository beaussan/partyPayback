package me.nbeaussart.payback.service;

import me.nbeaussart.payback.domain.InitialPayment;
import me.nbeaussart.payback.repository.InitialPaymentRepository;
import me.nbeaussart.payback.web.rest.dto.InitialPaymentDTO;
import me.nbeaussart.payback.web.rest.mapper.InitialPaymentMapper;
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
 * Service Implementation for managing InitialPayment.
 */
@Service
@Transactional
public class InitialPaymentService {

    private final Logger log = LoggerFactory.getLogger(InitialPaymentService.class);

    @Inject
    private InitialPaymentRepository initialPaymentRepository;

    @Inject
    private InitialPaymentMapper initialPaymentMapper;

    /**
     * Save a initialPayment.
     *
     * @param initialPaymentDTO the entity to save
     * @return the persisted entity
     */
    public InitialPaymentDTO save(InitialPaymentDTO initialPaymentDTO) {
        log.debug("Request to save InitialPayment : {}", initialPaymentDTO);
        InitialPayment initialPayment = initialPaymentMapper.initialPaymentDTOToInitialPayment(initialPaymentDTO);
        initialPayment = initialPaymentRepository.save(initialPayment);
        InitialPaymentDTO result = initialPaymentMapper.initialPaymentToInitialPaymentDTO(initialPayment);
        return result;
    }

    /**
     *  Get all the initialPayments.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InitialPayment> findAll(Pageable pageable) {
        log.debug("Request to get all InitialPayments");
        Page<InitialPayment> result = initialPaymentRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get all the initialPayment for a spesific event.
     *  @param id the event to read from
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<InitialPayment> findAllByEvent(Long id) {
        log.debug("Request to get all InitialPayments for event id : {}", id);
        List<InitialPayment> result = initialPaymentRepository.findByEventId(id);
        return result;
    }

    /**
     *  Get one initialPayment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public InitialPaymentDTO findOne(Long id) {
        log.debug("Request to get InitialPayment : {}", id);
        InitialPayment initialPayment = initialPaymentRepository.findOne(id);
        InitialPaymentDTO initialPaymentDTO = initialPaymentMapper.initialPaymentToInitialPaymentDTO(initialPayment);
        return initialPaymentDTO;
    }

    /**
     *  Delete the  initialPayment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InitialPayment : {}", id);
        initialPaymentRepository.delete(id);
    }

}
