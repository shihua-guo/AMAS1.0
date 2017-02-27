package com.binana.amas.web.rest;

import com.binana.amas.AmasApp;

import com.binana.amas.domain.Duty;
import com.binana.amas.repository.DutyRepository;
import com.binana.amas.repository.search.DutySearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.binana.amas.domain.enumeration.Weekday;
/**
 * Test class for the DutyResource REST controller.
 *
 * @see DutyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AmasApp.class)
public class DutyResourceIntTest {

    private static final Weekday DEFAULT_WEEKDAY = Weekday.MON;
    private static final Weekday UPDATED_WEEKDAY = Weekday.TUS;

    private static final String DEFAULT_DUTY_INTRODUTION = "AAAAAAAAAA";
    private static final String UPDATED_DUTY_INTRODUTION = "BBBBBBBBBB";

    @Autowired
    private DutyRepository dutyRepository;

    @Autowired
    private DutySearchRepository dutySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDutyMockMvc;

    private Duty duty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            DutyResource dutyResource = new DutyResource(dutyRepository, dutySearchRepository);
        this.restDutyMockMvc = MockMvcBuilders.standaloneSetup(dutyResource)
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
    public static Duty createEntity(EntityManager em) {
        Duty duty = new Duty()
                .weekday(DEFAULT_WEEKDAY)
                .dutyIntrodution(DEFAULT_DUTY_INTRODUTION);
        return duty;
    }

    @Before
    public void initTest() {
        dutySearchRepository.deleteAll();
        duty = createEntity(em);
    }

    @Test
    @Transactional
    public void createDuty() throws Exception {
        int databaseSizeBeforeCreate = dutyRepository.findAll().size();

        // Create the Duty

        restDutyMockMvc.perform(post("/api/duties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duty)))
            .andExpect(status().isCreated());

        // Validate the Duty in the database
        List<Duty> dutyList = dutyRepository.findAll();
        assertThat(dutyList).hasSize(databaseSizeBeforeCreate + 1);
        Duty testDuty = dutyList.get(dutyList.size() - 1);
        assertThat(testDuty.getWeekday()).isEqualTo(DEFAULT_WEEKDAY);
        assertThat(testDuty.getDutyIntrodution()).isEqualTo(DEFAULT_DUTY_INTRODUTION);

        // Validate the Duty in Elasticsearch
        Duty dutyEs = dutySearchRepository.findOne(testDuty.getId());
        assertThat(dutyEs).isEqualToComparingFieldByField(testDuty);
    }

    @Test
    @Transactional
    public void createDutyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dutyRepository.findAll().size();

        // Create the Duty with an existing ID
        Duty existingDuty = new Duty();
        existingDuty.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDutyMockMvc.perform(post("/api/duties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDuty)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Duty> dutyList = dutyRepository.findAll();
        assertThat(dutyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDuties() throws Exception {
        // Initialize the database
        dutyRepository.saveAndFlush(duty);

        // Get all the dutyList
        restDutyMockMvc.perform(get("/api/duties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(duty.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekday").value(hasItem(DEFAULT_WEEKDAY.toString())))
            .andExpect(jsonPath("$.[*].dutyIntrodution").value(hasItem(DEFAULT_DUTY_INTRODUTION.toString())));
    }

    @Test
    @Transactional
    public void getDuty() throws Exception {
        // Initialize the database
        dutyRepository.saveAndFlush(duty);

        // Get the duty
        restDutyMockMvc.perform(get("/api/duties/{id}", duty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(duty.getId().intValue()))
            .andExpect(jsonPath("$.weekday").value(DEFAULT_WEEKDAY.toString()))
            .andExpect(jsonPath("$.dutyIntrodution").value(DEFAULT_DUTY_INTRODUTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDuty() throws Exception {
        // Get the duty
        restDutyMockMvc.perform(get("/api/duties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDuty() throws Exception {
        // Initialize the database
        dutyRepository.saveAndFlush(duty);
        dutySearchRepository.save(duty);
        int databaseSizeBeforeUpdate = dutyRepository.findAll().size();

        // Update the duty
        Duty updatedDuty = dutyRepository.findOne(duty.getId());
        updatedDuty
                .weekday(UPDATED_WEEKDAY)
                .dutyIntrodution(UPDATED_DUTY_INTRODUTION);

        restDutyMockMvc.perform(put("/api/duties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDuty)))
            .andExpect(status().isOk());

        // Validate the Duty in the database
        List<Duty> dutyList = dutyRepository.findAll();
        assertThat(dutyList).hasSize(databaseSizeBeforeUpdate);
        Duty testDuty = dutyList.get(dutyList.size() - 1);
        assertThat(testDuty.getWeekday()).isEqualTo(UPDATED_WEEKDAY);
        assertThat(testDuty.getDutyIntrodution()).isEqualTo(UPDATED_DUTY_INTRODUTION);

        // Validate the Duty in Elasticsearch
        Duty dutyEs = dutySearchRepository.findOne(testDuty.getId());
        assertThat(dutyEs).isEqualToComparingFieldByField(testDuty);
    }

    @Test
    @Transactional
    public void updateNonExistingDuty() throws Exception {
        int databaseSizeBeforeUpdate = dutyRepository.findAll().size();

        // Create the Duty

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDutyMockMvc.perform(put("/api/duties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duty)))
            .andExpect(status().isCreated());

        // Validate the Duty in the database
        List<Duty> dutyList = dutyRepository.findAll();
        assertThat(dutyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDuty() throws Exception {
        // Initialize the database
        dutyRepository.saveAndFlush(duty);
        dutySearchRepository.save(duty);
        int databaseSizeBeforeDelete = dutyRepository.findAll().size();

        // Get the duty
        restDutyMockMvc.perform(delete("/api/duties/{id}", duty.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dutyExistsInEs = dutySearchRepository.exists(duty.getId());
        assertThat(dutyExistsInEs).isFalse();

        // Validate the database is empty
        List<Duty> dutyList = dutyRepository.findAll();
        assertThat(dutyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDuty() throws Exception {
        // Initialize the database
        dutyRepository.saveAndFlush(duty);
        dutySearchRepository.save(duty);

        // Search the duty
        restDutyMockMvc.perform(get("/api/_search/duties?query=id:" + duty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(duty.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekday").value(hasItem(DEFAULT_WEEKDAY.toString())))
            .andExpect(jsonPath("$.[*].dutyIntrodution").value(hasItem(DEFAULT_DUTY_INTRODUTION.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Duty.class);
    }
}
