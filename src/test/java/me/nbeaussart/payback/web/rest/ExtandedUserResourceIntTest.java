package me.nbeaussart.payback.web.rest;

import me.nbeaussart.payback.PartyPaybackApp;
import me.nbeaussart.payback.domain.ExtandedUser;
import me.nbeaussart.payback.repository.ExtandedUserRepository;
import me.nbeaussart.payback.service.ExtandedUserService;

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
 * Test class for the ExtandedUserResource REST controller.
 *
 * @see ExtandedUserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PartyPaybackApp.class)
@WebAppConfiguration
@IntegrationTest
public class ExtandedUserResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final Boolean DEFAULT_SENDIN_EMAIL = false;
    private static final Boolean UPDATED_SENDIN_EMAIL = true;

    @Inject
    private ExtandedUserRepository extandedUserRepository;

    @Inject
    private ExtandedUserService extandedUserService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExtandedUserMockMvc;

    private ExtandedUser extandedUser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExtandedUserResource extandedUserResource = new ExtandedUserResource();
        ReflectionTestUtils.setField(extandedUserResource, "extandedUserService", extandedUserService);
        this.restExtandedUserMockMvc = MockMvcBuilders.standaloneSetup(extandedUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        extandedUser = new ExtandedUser();
        extandedUser.setName(DEFAULT_NAME);
        extandedUser.setEmail(DEFAULT_EMAIL);
        extandedUser.setSendinEmail(DEFAULT_SENDIN_EMAIL);
    }

    @Test
    @Transactional
    public void createExtandedUser() throws Exception {
        int databaseSizeBeforeCreate = extandedUserRepository.findAll().size();

        // Create the ExtandedUser

        restExtandedUserMockMvc.perform(post("/api/extanded-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extandedUser)))
                .andExpect(status().isCreated());

        // Validate the ExtandedUser in the database
        List<ExtandedUser> extandedUsers = extandedUserRepository.findAll();
        assertThat(extandedUsers).hasSize(databaseSizeBeforeCreate + 1);
        ExtandedUser testExtandedUser = extandedUsers.get(extandedUsers.size() - 1);
        assertThat(testExtandedUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExtandedUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testExtandedUser.isSendinEmail()).isEqualTo(DEFAULT_SENDIN_EMAIL);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = extandedUserRepository.findAll().size();
        // set the field null
        extandedUser.setName(null);

        // Create the ExtandedUser, which fails.

        restExtandedUserMockMvc.perform(post("/api/extanded-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extandedUser)))
                .andExpect(status().isBadRequest());

        List<ExtandedUser> extandedUsers = extandedUserRepository.findAll();
        assertThat(extandedUsers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExtandedUsers() throws Exception {
        // Initialize the database
        extandedUserRepository.saveAndFlush(extandedUser);

        // Get all the extandedUsers
        restExtandedUserMockMvc.perform(get("/api/extanded-users?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(extandedUser.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].sendinEmail").value(hasItem(DEFAULT_SENDIN_EMAIL.booleanValue())));
    }

    @Test
    @Transactional
    public void getExtandedUser() throws Exception {
        // Initialize the database
        extandedUserRepository.saveAndFlush(extandedUser);

        // Get the extandedUser
        restExtandedUserMockMvc.perform(get("/api/extanded-users/{id}", extandedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(extandedUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.sendinEmail").value(DEFAULT_SENDIN_EMAIL.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExtandedUser() throws Exception {
        // Get the extandedUser
        restExtandedUserMockMvc.perform(get("/api/extanded-users/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtandedUser() throws Exception {
        // Initialize the database
        extandedUserService.save(extandedUser);

        int databaseSizeBeforeUpdate = extandedUserRepository.findAll().size();

        // Update the extandedUser
        ExtandedUser updatedExtandedUser = new ExtandedUser();
        updatedExtandedUser.setId(extandedUser.getId());
        updatedExtandedUser.setName(UPDATED_NAME);
        updatedExtandedUser.setEmail(UPDATED_EMAIL);
        updatedExtandedUser.setSendinEmail(UPDATED_SENDIN_EMAIL);

        restExtandedUserMockMvc.perform(put("/api/extanded-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedExtandedUser)))
                .andExpect(status().isOk());

        // Validate the ExtandedUser in the database
        List<ExtandedUser> extandedUsers = extandedUserRepository.findAll();
        assertThat(extandedUsers).hasSize(databaseSizeBeforeUpdate);
        ExtandedUser testExtandedUser = extandedUsers.get(extandedUsers.size() - 1);
        assertThat(testExtandedUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExtandedUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testExtandedUser.isSendinEmail()).isEqualTo(UPDATED_SENDIN_EMAIL);
    }

    @Test
    @Transactional
    public void deleteExtandedUser() throws Exception {
        // Initialize the database
        extandedUserService.save(extandedUser);

        int databaseSizeBeforeDelete = extandedUserRepository.findAll().size();

        // Get the extandedUser
        restExtandedUserMockMvc.perform(delete("/api/extanded-users/{id}", extandedUser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ExtandedUser> extandedUsers = extandedUserRepository.findAll();
        assertThat(extandedUsers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
