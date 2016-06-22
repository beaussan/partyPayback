package me.nbeaussart.payback.web.rest;

import me.nbeaussart.payback.PartyPaybackApp;
import me.nbeaussart.payback.domain.ExtandedUser;
import me.nbeaussart.payback.repository.ExtandedUserRepository;
import me.nbeaussart.payback.service.ExtandedUserService;
import me.nbeaussart.payback.web.rest.dto.ExtandedUserDTO;
import me.nbeaussart.payback.web.rest.mapper.ExtandedUserMapper;

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

import me.nbeaussart.payback.domain.enumeration.Genders;

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

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final Genders DEFAULT_GENDER = Genders.MALE;
    private static final Genders UPDATED_GENDER = Genders.FEMALE;

    @Inject
    private ExtandedUserRepository extandedUserRepository;

    @Inject
    private ExtandedUserMapper extandedUserMapper;

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
        ReflectionTestUtils.setField(extandedUserResource, "extandedUserMapper", extandedUserMapper);
        this.restExtandedUserMockMvc = MockMvcBuilders.standaloneSetup(extandedUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        extandedUser = new ExtandedUser();
        extandedUser.setDescription(DEFAULT_DESCRIPTION);
        extandedUser.setAge(DEFAULT_AGE);
        extandedUser.setGender(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    public void createExtandedUser() throws Exception {
        int databaseSizeBeforeCreate = extandedUserRepository.findAll().size();

        // Create the ExtandedUser
        ExtandedUserDTO extandedUserDTO = extandedUserMapper.extandedUserToExtandedUserDTO(extandedUser);

        restExtandedUserMockMvc.perform(post("/api/extanded-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extandedUserDTO)))
                .andExpect(status().isCreated());

        // Validate the ExtandedUser in the database
        List<ExtandedUser> extandedUsers = extandedUserRepository.findAll();
        assertThat(extandedUsers).hasSize(databaseSizeBeforeCreate + 1);
        ExtandedUser testExtandedUser = extandedUsers.get(extandedUsers.size() - 1);
        assertThat(testExtandedUser.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExtandedUser.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testExtandedUser.getGender()).isEqualTo(DEFAULT_GENDER);
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
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
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
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
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
        extandedUserRepository.saveAndFlush(extandedUser);
        int databaseSizeBeforeUpdate = extandedUserRepository.findAll().size();

        // Update the extandedUser
        ExtandedUser updatedExtandedUser = new ExtandedUser();
        updatedExtandedUser.setId(extandedUser.getId());
        updatedExtandedUser.setDescription(UPDATED_DESCRIPTION);
        updatedExtandedUser.setAge(UPDATED_AGE);
        updatedExtandedUser.setGender(UPDATED_GENDER);
        ExtandedUserDTO extandedUserDTO = extandedUserMapper.extandedUserToExtandedUserDTO(updatedExtandedUser);

        restExtandedUserMockMvc.perform(put("/api/extanded-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extandedUserDTO)))
                .andExpect(status().isOk());

        // Validate the ExtandedUser in the database
        List<ExtandedUser> extandedUsers = extandedUserRepository.findAll();
        assertThat(extandedUsers).hasSize(databaseSizeBeforeUpdate);
        ExtandedUser testExtandedUser = extandedUsers.get(extandedUsers.size() - 1);
        assertThat(testExtandedUser.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExtandedUser.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testExtandedUser.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void deleteExtandedUser() throws Exception {
        // Initialize the database
        extandedUserRepository.saveAndFlush(extandedUser);
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
