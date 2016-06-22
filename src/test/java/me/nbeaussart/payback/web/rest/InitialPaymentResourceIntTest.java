package me.nbeaussart.payback.web.rest;

import me.nbeaussart.payback.PartyPaybackApp;
import me.nbeaussart.payback.domain.InitialPayment;
import me.nbeaussart.payback.repository.InitialPaymentRepository;
import me.nbeaussart.payback.service.InitialPaymentService;
import me.nbeaussart.payback.web.rest.dto.InitialPaymentDTO;
import me.nbeaussart.payback.web.rest.mapper.InitialPaymentMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the InitialPaymentResource REST controller.
 *
 * @see InitialPaymentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PartyPaybackApp.class)
@WebAppConfiguration
@IntegrationTest
public class InitialPaymentResourceIntTest {


    private static final Long DEFAULT_AMMOUNT = 1L;
    private static final Long UPDATED_AMMOUNT = 2L;

    @Inject
    private InitialPaymentRepository initialPaymentRepository;

    @Inject
    private InitialPaymentMapper initialPaymentMapper;

    @Inject
    private InitialPaymentService initialPaymentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInitialPaymentMockMvc;

    private InitialPayment initialPayment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InitialPaymentResource initialPaymentResource = new InitialPaymentResource();
        ReflectionTestUtils.setField(initialPaymentResource, "initialPaymentService", initialPaymentService);
        ReflectionTestUtils.setField(initialPaymentResource, "initialPaymentMapper", initialPaymentMapper);
        this.restInitialPaymentMockMvc = MockMvcBuilders.standaloneSetup(initialPaymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        initialPayment = new InitialPayment();
        initialPayment.setAmmount(DEFAULT_AMMOUNT);
    }

    @Test
    @Transactional
    public void createInitialPayment() throws Exception {
        int databaseSizeBeforeCreate = initialPaymentRepository.findAll().size();

        // Create the InitialPayment
        InitialPaymentDTO initialPaymentDTO = initialPaymentMapper.initialPaymentToInitialPaymentDTO(initialPayment);

        restInitialPaymentMockMvc.perform(post("/api/initial-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(initialPaymentDTO)))
                .andExpect(status().isCreated());

        // Validate the InitialPayment in the database
        List<InitialPayment> initialPayments = initialPaymentRepository.findAll();
        assertThat(initialPayments).hasSize(databaseSizeBeforeCreate + 1);
        InitialPayment testInitialPayment = initialPayments.get(initialPayments.size() - 1);
        assertThat(testInitialPayment.getAmmount()).isEqualTo(DEFAULT_AMMOUNT);
    }

    @Test
    @Transactional
    public void getAllInitialPayments() throws Exception {
        // Initialize the database
        initialPaymentRepository.saveAndFlush(initialPayment);

        // Get all the initialPayments
        restInitialPaymentMockMvc.perform(get("/api/initial-payments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(initialPayment.getId().intValue())))
                .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getInitialPayment() throws Exception {
        // Initialize the database
        initialPaymentRepository.saveAndFlush(initialPayment);

        // Get the initialPayment
        restInitialPaymentMockMvc.perform(get("/api/initial-payments/{id}", initialPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(initialPayment.getId().intValue()))
            .andExpect(jsonPath("$.ammount").value(DEFAULT_AMMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInitialPayment() throws Exception {
        // Get the initialPayment
        restInitialPaymentMockMvc.perform(get("/api/initial-payments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInitialPayment() throws Exception {
        // Initialize the database
        initialPaymentRepository.saveAndFlush(initialPayment);
        int databaseSizeBeforeUpdate = initialPaymentRepository.findAll().size();

        // Update the initialPayment
        InitialPayment updatedInitialPayment = new InitialPayment();
        updatedInitialPayment.setId(initialPayment.getId());
        updatedInitialPayment.setAmmount(UPDATED_AMMOUNT);
        InitialPaymentDTO initialPaymentDTO = initialPaymentMapper.initialPaymentToInitialPaymentDTO(updatedInitialPayment);

        restInitialPaymentMockMvc.perform(put("/api/initial-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(initialPaymentDTO)))
                .andExpect(status().isOk());

        // Validate the InitialPayment in the database
        List<InitialPayment> initialPayments = initialPaymentRepository.findAll();
        assertThat(initialPayments).hasSize(databaseSizeBeforeUpdate);
        InitialPayment testInitialPayment = initialPayments.get(initialPayments.size() - 1);
        assertThat(testInitialPayment.getAmmount()).isEqualTo(UPDATED_AMMOUNT);
    }

    @Test
    @Transactional
    public void deleteInitialPayment() throws Exception {
        // Initialize the database
        initialPaymentRepository.saveAndFlush(initialPayment);
        int databaseSizeBeforeDelete = initialPaymentRepository.findAll().size();

        // Get the initialPayment
        restInitialPaymentMockMvc.perform(delete("/api/initial-payments/{id}", initialPayment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InitialPayment> initialPayments = initialPaymentRepository.findAll();
        assertThat(initialPayments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
