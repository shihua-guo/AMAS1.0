package com.binana.amas.web.rest;

import com.binana.amas.AmasApp;

import com.binana.amas.domain.Role;
import com.binana.amas.repository.RoleRepository;
import com.binana.amas.repository.search.RoleSearchRepository;
import com.binana.amas.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RoleResource REST controller.
 *
 * @see RoleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AmasApp.class)
public class RoleResourceIntTest {

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleSearchRepository roleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRoleMockMvc;

    private Role role;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            RoleResource roleResource = new RoleResource(roleRepository, roleSearchRepository);
        this.restRoleMockMvc = MockMvcBuilders.standaloneSetup(roleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createEntity(EntityManager em) {
        Role role = new Role()
                .roleName(DEFAULT_ROLE_NAME);
        return role;
    }

    @Before
    public void initTest() {
        roleSearchRepository.deleteAll();
        role = createEntity(em);
    }

    @Test
    @Transactional
    public void createRole() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // Create the Role

        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);

        // Validate the Role in Elasticsearch
        Role roleEs = roleSearchRepository.findOne(testRole.getId());
        assertThat(roleEs).isEqualToComparingFieldByField(testRole);
    }

    @Test
    @Transactional
    public void createRoleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // Create the Role with an existing ID
        Role existingRole = new Role();
        existingRole.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingRole)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRoleNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleRepository.findAll().size();
        // set the field null
        role.setRoleName(null);

        // Create the Role, which fails.

        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isBadRequest());

        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoles() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList
        restRoleMockMvc.perform(get("/api/roles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(role.getId().intValue()))
            .andExpect(jsonPath("$.roleName").value(DEFAULT_ROLE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);
        roleSearchRepository.save(role);
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role
        Role updatedRole = roleRepository.findOne(role.getId());
        updatedRole
                .roleName(UPDATED_ROLE_NAME);

        restRoleMockMvc.perform(put("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRole)))
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);

        // Validate the Role in Elasticsearch
        Role roleEs = roleSearchRepository.findOne(testRole.getId());
        assertThat(roleEs).isEqualToComparingFieldByField(testRole);
    }

    @Test
    @Transactional
    public void updateNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Create the Role

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRoleMockMvc.perform(put("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);
        roleSearchRepository.save(role);
        int databaseSizeBeforeDelete = roleRepository.findAll().size();

        // Get the role
        restRoleMockMvc.perform(delete("/api/roles/{id}", role.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean roleExistsInEs = roleSearchRepository.exists(role.getId());
        assertThat(roleExistsInEs).isFalse();

        // Validate the database is empty
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);
        roleSearchRepository.save(role);

        // Search the role
        restRoleMockMvc.perform(get("/api/_search/roles?query=id:" + role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Role.class);
    }
}
