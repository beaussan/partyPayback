package me.nbeaussart.payback.web.rest;

import me.nbeaussart.payback.PartyPaybackApp;
import me.nbeaussart.payback.domain.PayBack;
import me.nbeaussart.payback.repository.PayBackRepository;
import me.nbeaussart.payback.service.PayBackService;
import me.nbeaussart.payback.web.rest.dto.PayBackDTO;
import me.nbeaussart.payback.web.rest.mapper.PayBackMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PayBackResource REST controller.
 *
 * @see PayBackResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PartyPaybackApp.class)
@WebAppConfiguration
@IntegrationTest
public class PayBackResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Double DEFAULT_AMMOUNT = 1D;
    private static final Double UPDATED_AMMOUNT = 2D;

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIMESTAMP_STR = dateTimeFormatter.format(DEFAULT_TIMESTAMP);

    private static final Boolean DEFAULT_IS_PAID = false;
    private static final Boolean UPDATED_IS_PAID = true;

    @Inject
    private PayBackRepository payBackRepository;

    @Inject
    private PayBackMapper payBackMapper;

    @Inject
    private PayBackService payBackService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPayBackMockMvc;

    private PayBack payBack;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PayBackResource payBackResource = new PayBackResource();
        ReflectionTestUtils.setField(payBackResource, "payBackService", payBackService);
        ReflectionTestUtils.setField(payBackResource, "payBackMapper", payBackMapper);
        this.restPayBackMockMvc = MockMvcBuilders.standaloneSetup(payBackResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        payBack = new PayBack();
        payBack.setAmmount(DEFAULT_AMMOUNT);
        payBack.setTimestamp(DEFAULT_TIMESTAMP);
        payBack.setIsPaid(DEFAULT_IS_PAID);
    }

    @Test
    @Transactional
    public void createPayBack() throws Exception {
        int databaseSizeBeforeCreate = payBackRepository.findAll().size();

        // Create the PayBack
        PayBackDTO payBackDTO = payBackMapper.payBackToPayBackDTO(payBack);

        restPayBackMockMvc.perform(post("/api/pay-backs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(payBackDTO)))
                .andExpect(status().isCreated());

        // Validate the PayBack in the database
        List<PayBack> payBacks = payBackRepository.findAll();
        assertThat(payBacks).hasSize(databaseSizeBeforeCreate + 1);
        PayBack testPayBack = payBacks.get(payBacks.size() - 1);
        assertThat(testPayBack.getAmmount()).isEqualTo(DEFAULT_AMMOUNT);
        assertThat(testPayBack.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testPayBack.isIsPaid()).isEqualTo(DEFAULT_IS_PAID);
    }

    @Test
    @Transactional
    public void getAllPayBacks() throws Exception {
        // Initialize the database
        payBackRepository.saveAndFlush(payBack);

        // Get all the payBacks
        restPayBackMockMvc.perform(get("/api/pay-backs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(payBack.getId().intValue())))
                .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP_STR)))
                .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID.booleanValue())));
    }

    @Test
    @Transactional
    public void getPayBack() throws Exception {
        // Initialize the database
        payBackRepository.saveAndFlush(payBack);

        // Get the payBack
        restPayBackMockMvc.perform(get("/api/pay-backs/{id}", payBack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(payBack.getId().intValue()))
            .andExpect(jsonPath("$.ammount").value(DEFAULT_AMMOUNT.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP_STR))
            .andExpect(jsonPath("$.isPaid").value(DEFAULT_IS_PAID.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPayBack() throws Exception {
        // Get the payBack
        restPayBackMockMvc.perform(get("/api/pay-backs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayBack() throws Exception {
        // Initialize the database
        payBackRepository.saveAndFlush(payBack);
        int databaseSizeBeforeUpdate = payBackRepository.findAll().size();

        // Update the payBack
        PayBack updatedPayBack = new PayBack();
        updatedPayBack.setId(payBack.getId());
        updatedPayBack.setAmmount(UPDATED_AMMOUNT);
        updatedPayBack.setTimestamp(UPDATED_TIMESTAMP);
        updatedPayBack.setIsPaid(UPDATED_IS_PAID);
        PayBackDTO payBackDTO = payBackMapper.payBackToPayBackDTO(updatedPayBack);

        restPayBackMockMvc.perform(put("/api/pay-backs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(payBackDTO)))
                .andExpect(status().isOk());

        // Validate the PayBack in the database
        List<PayBack> payBacks = payBackRepository.findAll();
        assertThat(payBacks).hasSize(databaseSizeBeforeUpdate);
        PayBack testPayBack = payBacks.get(payBacks.size() - 1);
        assertThat(testPayBack.getAmmount()).isEqualTo(UPDATED_AMMOUNT);
        assertThat(testPayBack.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testPayBack.isIsPaid()).isEqualTo(UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    public void deletePayBack() throws Exception {
        // Initialize the database
        payBackRepository.saveAndFlush(payBack);
        int databaseSizeBeforeDelete = payBackRepository.findAll().size();

        // Get the payBack
        restPayBackMockMvc.perform(delete("/api/pay-backs/{id}", payBack.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PayBack> payBacks = payBackRepository.findAll();
        assertThat(payBacks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
