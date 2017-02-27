package com.binana.amas.web.rest;

import com.binana.amas.AmasApp;

import com.binana.amas.domain.Association;
import com.binana.amas.repository.AssociationRepository;
import com.binana.amas.repository.search.AssociationSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AssociationResource REST controller.
 *
 * @see AssociationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AmasApp.class)
public class AssociationResourceIntTest {

    private static final String DEFAULT_ASSO_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSO_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSO_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSO_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ASSO_FOUND_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ASSO_FOUND_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ASSO_INTRODUTION = "AAAAAAAAAA";
    private static final String UPDATED_ASSO_INTRODUTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ASSO_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ASSO_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ASSO_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ASSO_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private AssociationRepository associationRepository;

    @Autowired
    private AssociationSearchRepository associationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAssociationMockMvc;

    private Association association;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            AssociationResource associationResource = new AssociationResource(associationRepository, associationSearchRepository);
        this.restAssociationMockMvc = MockMvcBuilders.standaloneSetup(associationResource)
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
    public static Association createEntity(EntityManager em) {
        Association association = new Association()
                .assoId(DEFAULT_ASSO_ID)
                .assoName(DEFAULT_ASSO_NAME)
                .assoFoundDate(DEFAULT_ASSO_FOUND_DATE)
                .assoIntrodution(DEFAULT_ASSO_INTRODUTION)
                .assoImage(DEFAULT_ASSO_IMAGE)
                .assoImageContentType(DEFAULT_ASSO_IMAGE_CONTENT_TYPE);
        return association;
    }

    @Before
    public void initTest() {
        associationSearchRepository.deleteAll();
        association = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssociation() throws Exception {
        int databaseSizeBeforeCreate = associationRepository.findAll().size();

        // Create the Association

        restAssociationMockMvc.perform(post("/api/associations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(association)))
            .andExpect(status().isCreated());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeCreate + 1);
        Association testAssociation = associationList.get(associationList.size() - 1);
        assertThat(testAssociation.getAssoId()).isEqualTo(DEFAULT_ASSO_ID);
        assertThat(testAssociation.getAssoName()).isEqualTo(DEFAULT_ASSO_NAME);
        assertThat(testAssociation.getAssoFoundDate()).isEqualTo(DEFAULT_ASSO_FOUND_DATE);
        assertThat(testAssociation.getAssoIntrodution()).isEqualTo(DEFAULT_ASSO_INTRODUTION);
        assertThat(testAssociation.getAssoImage()).isEqualTo(DEFAULT_ASSO_IMAGE);
        assertThat(testAssociation.getAssoImageContentType()).isEqualTo(DEFAULT_ASSO_IMAGE_CONTENT_TYPE);

        // Validate the Association in Elasticsearch
        Association associationEs = associationSearchRepository.findOne(testAssociation.getId());
        assertThat(associationEs).isEqualToComparingFieldByField(testAssociation);
    }

    @Test
    @Transactional
    public void createAssociationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = associationRepository.findAll().size();

        // Create the Association with an existing ID
        Association existingAssociation = new Association();
        existingAssociation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssociationMockMvc.perform(post("/api/associations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAssociation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAssoIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = associationRepository.findAll().size();
        // set the field null
        association.setAssoId(null);

        // Create the Association, which fails.

        restAssociationMockMvc.perform(post("/api/associations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(association)))
            .andExpect(status().isBadRequest());

        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssoNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = associationRepository.findAll().size();
        // set the field null
        association.setAssoName(null);

        // Create the Association, which fails.

        restAssociationMockMvc.perform(post("/api/associations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(association)))
            .andExpect(status().isBadRequest());

        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssoFoundDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = associationRepository.findAll().size();
        // set the field null
        association.setAssoFoundDate(null);

        // Create the Association, which fails.

        restAssociationMockMvc.perform(post("/api/associations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(association)))
            .andExpect(status().isBadRequest());

        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssociations() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList
        restAssociationMockMvc.perform(get("/api/associations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(association.getId().intValue())))
            .andExpect(jsonPath("$.[*].assoId").value(hasItem(DEFAULT_ASSO_ID.toString())))
            .andExpect(jsonPath("$.[*].assoName").value(hasItem(DEFAULT_ASSO_NAME.toString())))
            .andExpect(jsonPath("$.[*].assoFoundDate").value(hasItem(DEFAULT_ASSO_FOUND_DATE.toString())))
            .andExpect(jsonPath("$.[*].assoIntrodution").value(hasItem(DEFAULT_ASSO_INTRODUTION.toString())))
            .andExpect(jsonPath("$.[*].assoImageContentType").value(hasItem(DEFAULT_ASSO_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].assoImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ASSO_IMAGE))));
    }

    @Test
    @Transactional
    public void getAssociation() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get the association
        restAssociationMockMvc.perform(get("/api/associations/{id}", association.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(association.getId().intValue()))
            .andExpect(jsonPath("$.assoId").value(DEFAULT_ASSO_ID.toString()))
            .andExpect(jsonPath("$.assoName").value(DEFAULT_ASSO_NAME.toString()))
            .andExpect(jsonPath("$.assoFoundDate").value(DEFAULT_ASSO_FOUND_DATE.toString()))
            .andExpect(jsonPath("$.assoIntrodution").value(DEFAULT_ASSO_INTRODUTION.toString()))
            .andExpect(jsonPath("$.assoImageContentType").value(DEFAULT_ASSO_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.assoImage").value(Base64Utils.encodeToString(DEFAULT_ASSO_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingAssociation() throws Exception {
        // Get the association
        restAssociationMockMvc.perform(get("/api/associations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssociation() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);
        associationSearchRepository.save(association);
        int databaseSizeBeforeUpdate = associationRepository.findAll().size();

        // Update the association
        Association updatedAssociation = associationRepository.findOne(association.getId());
        updatedAssociation
                .assoId(UPDATED_ASSO_ID)
                .assoName(UPDATED_ASSO_NAME)
                .assoFoundDate(UPDATED_ASSO_FOUND_DATE)
                .assoIntrodution(UPDATED_ASSO_INTRODUTION)
                .assoImage(UPDATED_ASSO_IMAGE)
                .assoImageContentType(UPDATED_ASSO_IMAGE_CONTENT_TYPE);

        restAssociationMockMvc.perform(put("/api/associations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAssociation)))
            .andExpect(status().isOk());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
        Association testAssociation = associationList.get(associationList.size() - 1);
        assertThat(testAssociation.getAssoId()).isEqualTo(UPDATED_ASSO_ID);
        assertThat(testAssociation.getAssoName()).isEqualTo(UPDATED_ASSO_NAME);
        assertThat(testAssociation.getAssoFoundDate()).isEqualTo(UPDATED_ASSO_FOUND_DATE);
        assertThat(testAssociation.getAssoIntrodution()).isEqualTo(UPDATED_ASSO_INTRODUTION);
        assertThat(testAssociation.getAssoImage()).isEqualTo(UPDATED_ASSO_IMAGE);
        assertThat(testAssociation.getAssoImageContentType()).isEqualTo(UPDATED_ASSO_IMAGE_CONTENT_TYPE);

        // Validate the Association in Elasticsearch
        Association associationEs = associationSearchRepository.findOne(testAssociation.getId());
        assertThat(associationEs).isEqualToComparingFieldByField(testAssociation);
    }

    @Test
    @Transactional
    public void updateNonExistingAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().size();

        // Create the Association

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAssociationMockMvc.perform(put("/api/associations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(association)))
            .andExpect(status().isCreated());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAssociation() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);
        associationSearchRepository.save(association);
        int databaseSizeBeforeDelete = associationRepository.findAll().size();

        // Get the association
        restAssociationMockMvc.perform(delete("/api/associations/{id}", association.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean associationExistsInEs = associationSearchRepository.exists(association.getId());
        assertThat(associationExistsInEs).isFalse();

        // Validate the database is empty
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAssociation() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);
        associationSearchRepository.save(association);

        // Search the association
        restAssociationMockMvc.perform(get("/api/_search/associations?query=id:" + association.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(association.getId().intValue())))
            .andExpect(jsonPath("$.[*].assoId").value(hasItem(DEFAULT_ASSO_ID.toString())))
            .andExpect(jsonPath("$.[*].assoName").value(hasItem(DEFAULT_ASSO_NAME.toString())))
            .andExpect(jsonPath("$.[*].assoFoundDate").value(hasItem(DEFAULT_ASSO_FOUND_DATE.toString())))
            .andExpect(jsonPath("$.[*].assoIntrodution").value(hasItem(DEFAULT_ASSO_INTRODUTION.toString())))
            .andExpect(jsonPath("$.[*].assoImageContentType").value(hasItem(DEFAULT_ASSO_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].assoImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ASSO_IMAGE))));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Association.class);
    }
}
