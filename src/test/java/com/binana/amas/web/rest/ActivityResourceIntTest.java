package com.binana.amas.web.rest;

import com.binana.amas.AmasApp;

import com.binana.amas.domain.Activity;
import com.binana.amas.repository.ActivityRepository;
import com.binana.amas.repository.search.ActivitySearchRepository;
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
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AmasApp.class)
public class ActivityResourceIntTest {

    private static final String DEFAULT_ACTI_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACTI_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ACTI_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTI_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ACTI_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_ACTI_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTI_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_ACTI_CONTENT = "BBBBBBBBBB";

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivitySearchRepository activitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivityMockMvc;

    private Activity activity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            ActivityResource activityResource = new ActivityResource(activityRepository, activitySearchRepository);
        this.restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource)
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
    public static Activity createEntity(EntityManager em) {
        Activity activity = new Activity()
                .actiName(DEFAULT_ACTI_NAME)
                .actiDate(DEFAULT_ACTI_DATE)
                .actiPlace(DEFAULT_ACTI_PLACE)
                .actiContent(DEFAULT_ACTI_CONTENT);
        return activity;
    }

    @Before
    public void initTest() {
        activitySearchRepository.deleteAll();
        activity = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivity() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().size();

        // Create the Activity

        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activity)))
            .andExpect(status().isCreated());

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeCreate + 1);
        Activity testActivity = activityList.get(activityList.size() - 1);
        assertThat(testActivity.getActiName()).isEqualTo(DEFAULT_ACTI_NAME);
        assertThat(testActivity.getActiDate()).isEqualTo(DEFAULT_ACTI_DATE);
        assertThat(testActivity.getActiPlace()).isEqualTo(DEFAULT_ACTI_PLACE);
        assertThat(testActivity.getActiContent()).isEqualTo(DEFAULT_ACTI_CONTENT);

        // Validate the Activity in Elasticsearch
        Activity activityEs = activitySearchRepository.findOne(testActivity.getId());
        assertThat(activityEs).isEqualToComparingFieldByField(testActivity);
    }

    @Test
    @Transactional
    public void createActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().size();

        // Create the Activity with an existing ID
        Activity existingActivity = new Activity();
        existingActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingActivity)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkActiNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityRepository.findAll().size();
        // set the field null
        activity.setActiName(null);

        // Create the Activity, which fails.

        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activity)))
            .andExpect(status().isBadRequest());

        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityRepository.findAll().size();
        // set the field null
        activity.setActiDate(null);

        // Create the Activity, which fails.

        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activity)))
            .andExpect(status().isBadRequest());

        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiPlaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityRepository.findAll().size();
        // set the field null
        activity.setActiPlace(null);

        // Create the Activity, which fails.

        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activity)))
            .andExpect(status().isBadRequest());

        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActivities() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList
        restActivityMockMvc.perform(get("/api/activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
            .andExpect(jsonPath("$.[*].actiName").value(hasItem(DEFAULT_ACTI_NAME.toString())))
            .andExpect(jsonPath("$.[*].actiDate").value(hasItem(DEFAULT_ACTI_DATE.toString())))
            .andExpect(jsonPath("$.[*].actiPlace").value(hasItem(DEFAULT_ACTI_PLACE.toString())))
            .andExpect(jsonPath("$.[*].actiContent").value(hasItem(DEFAULT_ACTI_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get the activity
        restActivityMockMvc.perform(get("/api/activities/{id}", activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activity.getId().intValue()))
            .andExpect(jsonPath("$.actiName").value(DEFAULT_ACTI_NAME.toString()))
            .andExpect(jsonPath("$.actiDate").value(DEFAULT_ACTI_DATE.toString()))
            .andExpect(jsonPath("$.actiPlace").value(DEFAULT_ACTI_PLACE.toString()))
            .andExpect(jsonPath("$.actiContent").value(DEFAULT_ACTI_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActivity() throws Exception {
        // Get the activity
        restActivityMockMvc.perform(get("/api/activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);
        activitySearchRepository.save(activity);
        int databaseSizeBeforeUpdate = activityRepository.findAll().size();

        // Update the activity
        Activity updatedActivity = activityRepository.findOne(activity.getId());
        updatedActivity
                .actiName(UPDATED_ACTI_NAME)
                .actiDate(UPDATED_ACTI_DATE)
                .actiPlace(UPDATED_ACTI_PLACE)
                .actiContent(UPDATED_ACTI_CONTENT);

        restActivityMockMvc.perform(put("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActivity)))
            .andExpect(status().isOk());

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activityList.get(activityList.size() - 1);
        assertThat(testActivity.getActiName()).isEqualTo(UPDATED_ACTI_NAME);
        assertThat(testActivity.getActiDate()).isEqualTo(UPDATED_ACTI_DATE);
        assertThat(testActivity.getActiPlace()).isEqualTo(UPDATED_ACTI_PLACE);
        assertThat(testActivity.getActiContent()).isEqualTo(UPDATED_ACTI_CONTENT);

        // Validate the Activity in Elasticsearch
        Activity activityEs = activitySearchRepository.findOne(testActivity.getId());
        assertThat(activityEs).isEqualToComparingFieldByField(testActivity);
    }

    @Test
    @Transactional
    public void updateNonExistingActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().size();

        // Create the Activity

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActivityMockMvc.perform(put("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activity)))
            .andExpect(status().isCreated());

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);
        activitySearchRepository.save(activity);
        int databaseSizeBeforeDelete = activityRepository.findAll().size();

        // Get the activity
        restActivityMockMvc.perform(delete("/api/activities/{id}", activity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean activityExistsInEs = activitySearchRepository.exists(activity.getId());
        assertThat(activityExistsInEs).isFalse();

        // Validate the database is empty
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);
        activitySearchRepository.save(activity);

        // Search the activity
        restActivityMockMvc.perform(get("/api/_search/activities?query=id:" + activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
            .andExpect(jsonPath("$.[*].actiName").value(hasItem(DEFAULT_ACTI_NAME.toString())))
            .andExpect(jsonPath("$.[*].actiDate").value(hasItem(DEFAULT_ACTI_DATE.toString())))
            .andExpect(jsonPath("$.[*].actiPlace").value(hasItem(DEFAULT_ACTI_PLACE.toString())))
            .andExpect(jsonPath("$.[*].actiContent").value(hasItem(DEFAULT_ACTI_CONTENT.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activity.class);
    }
}
