package com.binana.amas.web.rest;

import com.binana.amas.AmasApp;

import com.binana.amas.domain.Property;
import com.binana.amas.repository.PropertyRepository;
import com.binana.amas.repository.search.PropertySearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PropertyResource REST controller.
 *
 * @see PropertyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AmasApp.class)
public class PropertyResourceIntTest {

    private static final String DEFAULT_PROP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROP_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PROP_PRICE = 1D;
    private static final Double UPDATED_PROP_PRICE = 2D;

    private static final LocalDate DEFAULT_PROP_BUY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROP_BUY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_PROP_NUMBER = 1L;
    private static final Long UPDATED_PROP_NUMBER = 2L;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertySearchRepository propertySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPropertyMockMvc;

    private Property property;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            PropertyResource propertyResource = new PropertyResource(propertyRepository, propertySearchRepository);
        this.restPropertyMockMvc = MockMvcBuilders.standaloneSetup(propertyResource)
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
    public static Property createEntity(EntityManager em) {
        Property property = new Property()
                .propName(DEFAULT_PROP_NAME)
                .propPrice(DEFAULT_PROP_PRICE)
                .propBuyDate(DEFAULT_PROP_BUY_DATE)
                .propNumber(DEFAULT_PROP_NUMBER);
        return property;
    }

    @Before
    public void initTest() {
        propertySearchRepository.deleteAll();
        property = createEntity(em);
    }

    @Test
    @Transactional
    public void createProperty() throws Exception {
        int databaseSizeBeforeCreate = propertyRepository.findAll().size();

        // Create the Property

        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isCreated());

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeCreate + 1);
        Property testProperty = propertyList.get(propertyList.size() - 1);
        assertThat(testProperty.getPropName()).isEqualTo(DEFAULT_PROP_NAME);
        assertThat(testProperty.getPropPrice()).isEqualTo(DEFAULT_PROP_PRICE);
        assertThat(testProperty.getPropBuyDate()).isEqualTo(DEFAULT_PROP_BUY_DATE);
        assertThat(testProperty.getPropNumber()).isEqualTo(DEFAULT_PROP_NUMBER);

        // Validate the Property in Elasticsearch
        Property propertyEs = propertySearchRepository.findOne(testProperty.getId());
        assertThat(propertyEs).isEqualToComparingFieldByField(testProperty);
    }

    @Test
    @Transactional
    public void createPropertyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propertyRepository.findAll().size();

        // Create the Property with an existing ID
        Property existingProperty = new Property();
        existingProperty.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProperty)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPropNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().size();
        // set the field null
        property.setPropName(null);

        // Create the Property, which fails.

        restPropertyMockMvc.perform(post("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isBadRequest());

        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProperties() throws Exception {
        // Initialize the database
        propertyRepository.saveAndFlush(property);

        // Get all the propertyList
        restPropertyMockMvc.perform(get("/api/properties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(property.getId().intValue())))
            .andExpect(jsonPath("$.[*].propName").value(hasItem(DEFAULT_PROP_NAME.toString())))
            .andExpect(jsonPath("$.[*].propPrice").value(hasItem(DEFAULT_PROP_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].propBuyDate").value(hasItem(DEFAULT_PROP_BUY_DATE.toString())))
            .andExpect(jsonPath("$.[*].propNumber").value(hasItem(DEFAULT_PROP_NUMBER.intValue())));
    }

    @Test
    @Transactional
    public void getProperty() throws Exception {
        // Initialize the database
        propertyRepository.saveAndFlush(property);

        // Get the property
        restPropertyMockMvc.perform(get("/api/properties/{id}", property.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(property.getId().intValue()))
            .andExpect(jsonPath("$.propName").value(DEFAULT_PROP_NAME.toString()))
            .andExpect(jsonPath("$.propPrice").value(DEFAULT_PROP_PRICE.doubleValue()))
            .andExpect(jsonPath("$.propBuyDate").value(DEFAULT_PROP_BUY_DATE.toString()))
            .andExpect(jsonPath("$.propNumber").value(DEFAULT_PROP_NUMBER.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProperty() throws Exception {
        // Get the property
        restPropertyMockMvc.perform(get("/api/properties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProperty() throws Exception {
        // Initialize the database
        propertyRepository.saveAndFlush(property);
        propertySearchRepository.save(property);
        int databaseSizeBeforeUpdate = propertyRepository.findAll().size();

        // Update the property
        Property updatedProperty = propertyRepository.findOne(property.getId());
        updatedProperty
                .propName(UPDATED_PROP_NAME)
                .propPrice(UPDATED_PROP_PRICE)
                .propBuyDate(UPDATED_PROP_BUY_DATE)
                .propNumber(UPDATED_PROP_NUMBER);

        restPropertyMockMvc.perform(put("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProperty)))
            .andExpect(status().isOk());

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
        Property testProperty = propertyList.get(propertyList.size() - 1);
        assertThat(testProperty.getPropName()).isEqualTo(UPDATED_PROP_NAME);
        assertThat(testProperty.getPropPrice()).isEqualTo(UPDATED_PROP_PRICE);
        assertThat(testProperty.getPropBuyDate()).isEqualTo(UPDATED_PROP_BUY_DATE);
        assertThat(testProperty.getPropNumber()).isEqualTo(UPDATED_PROP_NUMBER);

        // Validate the Property in Elasticsearch
        Property propertyEs = propertySearchRepository.findOne(testProperty.getId());
        assertThat(propertyEs).isEqualToComparingFieldByField(testProperty);
    }

    @Test
    @Transactional
    public void updateNonExistingProperty() throws Exception {
        int databaseSizeBeforeUpdate = propertyRepository.findAll().size();

        // Create the Property

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPropertyMockMvc.perform(put("/api/properties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(property)))
            .andExpect(status().isCreated());

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProperty() throws Exception {
        // Initialize the database
        propertyRepository.saveAndFlush(property);
        propertySearchRepository.save(property);
        int databaseSizeBeforeDelete = propertyRepository.findAll().size();

        // Get the property
        restPropertyMockMvc.perform(delete("/api/properties/{id}", property.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean propertyExistsInEs = propertySearchRepository.exists(property.getId());
        assertThat(propertyExistsInEs).isFalse();

        // Validate the database is empty
        List<Property> propertyList = propertyRepository.findAll();
        assertThat(propertyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProperty() throws Exception {
        // Initialize the database
        propertyRepository.saveAndFlush(property);
        propertySearchRepository.save(property);

        // Search the property
        restPropertyMockMvc.perform(get("/api/_search/properties?query=id:" + property.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(property.getId().intValue())))
            .andExpect(jsonPath("$.[*].propName").value(hasItem(DEFAULT_PROP_NAME.toString())))
            .andExpect(jsonPath("$.[*].propPrice").value(hasItem(DEFAULT_PROP_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].propBuyDate").value(hasItem(DEFAULT_PROP_BUY_DATE.toString())))
            .andExpect(jsonPath("$.[*].propNumber").value(hasItem(DEFAULT_PROP_NUMBER.intValue())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Property.class);
    }
}
