package com.binana.amas.web.rest;

import com.binana.amas.AmasApp;

import com.binana.amas.domain.Amember;
import com.binana.amas.repository.AmemberRepository;
import com.binana.amas.repository.search.AmemberSearchRepository;
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

import com.binana.amas.domain.enumeration.GENDER;
import com.binana.amas.domain.enumeration.POLITICSSTATUS;
/**
 * Test class for the AmemberResource REST controller.
 *
 * @see AmemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AmasApp.class)
public class AmemberResourceIntTest {

    private static final String DEFAULT_MEMB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MEMB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MEMB_NO = "AAAAAAAAAA";
    private static final String UPDATED_MEMB_NO = "BBBBBBBBBB";

    private static final String DEFAULT_MEMB_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_MEMB_CLASS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMB_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MEMB_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MEMB_QQ = "AAAAAAAAAA";
    private static final String UPDATED_MEMB_QQ = "BBBBBBBBBB";

    private static final String DEFAULT_MEMB_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_MEMB_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MEMB_JOIN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MEMB_JOIN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final GENDER DEFAULT_GENDER = GENDER.M;
    private static final GENDER UPDATED_GENDER = GENDER.F;

    private static final String DEFAULT_DORM_NUM = "AAAAAAAAAA";
    private static final String UPDATED_DORM_NUM = "BBBBBBBBBB";

    private static final POLITICSSTATUS DEFAULT_POLITICS_STATUS = POLITICSSTATUS.PARTY;
    private static final POLITICSSTATUS UPDATED_POLITICS_STATUS = POLITICSSTATUS.LEAGUE;

    @Autowired
    private AmemberRepository amemberRepository;

    @Autowired
    private AmemberSearchRepository amemberSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAmemberMockMvc;

    private Amember amember;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            AmemberResource amemberResource = new AmemberResource(amemberRepository, amemberSearchRepository);
        this.restAmemberMockMvc = MockMvcBuilders.standaloneSetup(amemberResource)
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
    public static Amember createEntity(EntityManager em) {
        Amember amember = new Amember()
                .membName(DEFAULT_MEMB_NAME)
                .membNO(DEFAULT_MEMB_NO)
                .membClass(DEFAULT_MEMB_CLASS)
                .membPhone(DEFAULT_MEMB_PHONE)
                .membQQ(DEFAULT_MEMB_QQ)
                .membEmail(DEFAULT_MEMB_EMAIL)
                .membJoinDate(DEFAULT_MEMB_JOIN_DATE)
                .gender(DEFAULT_GENDER)
                .dormNum(DEFAULT_DORM_NUM)
                .politicsStatus(DEFAULT_POLITICS_STATUS);
        return amember;
    }

    @Before
    public void initTest() {
        amemberSearchRepository.deleteAll();
        amember = createEntity(em);
    }

    @Test
    @Transactional
    public void createAmember() throws Exception {
        int databaseSizeBeforeCreate = amemberRepository.findAll().size();

        // Create the Amember

        restAmemberMockMvc.perform(post("/api/amembers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amember)))
            .andExpect(status().isCreated());

        // Validate the Amember in the database
        List<Amember> amemberList = amemberRepository.findAll();
        assertThat(amemberList).hasSize(databaseSizeBeforeCreate + 1);
        Amember testAmember = amemberList.get(amemberList.size() - 1);
        assertThat(testAmember.getMembName()).isEqualTo(DEFAULT_MEMB_NAME);
        assertThat(testAmember.getMembNO()).isEqualTo(DEFAULT_MEMB_NO);
        assertThat(testAmember.getMembClass()).isEqualTo(DEFAULT_MEMB_CLASS);
        assertThat(testAmember.getMembPhone()).isEqualTo(DEFAULT_MEMB_PHONE);
        assertThat(testAmember.getMembQQ()).isEqualTo(DEFAULT_MEMB_QQ);
        assertThat(testAmember.getMembEmail()).isEqualTo(DEFAULT_MEMB_EMAIL);
        assertThat(testAmember.getMembJoinDate()).isEqualTo(DEFAULT_MEMB_JOIN_DATE);
        assertThat(testAmember.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAmember.getDormNum()).isEqualTo(DEFAULT_DORM_NUM);
        assertThat(testAmember.getPoliticsStatus()).isEqualTo(DEFAULT_POLITICS_STATUS);

        // Validate the Amember in Elasticsearch
        Amember amemberEs = amemberSearchRepository.findOne(testAmember.getId());
        assertThat(amemberEs).isEqualToComparingFieldByField(testAmember);
    }

    @Test
    @Transactional
    public void createAmemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = amemberRepository.findAll().size();

        // Create the Amember with an existing ID
        Amember existingAmember = new Amember();
        existingAmember.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmemberMockMvc.perform(post("/api/amembers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAmember)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Amember> amemberList = amemberRepository.findAll();
        assertThat(amemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMembNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = amemberRepository.findAll().size();
        // set the field null
        amember.setMembName(null);

        // Create the Amember, which fails.

        restAmemberMockMvc.perform(post("/api/amembers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amember)))
            .andExpect(status().isBadRequest());

        List<Amember> amemberList = amemberRepository.findAll();
        assertThat(amemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMembNOIsRequired() throws Exception {
        int databaseSizeBeforeTest = amemberRepository.findAll().size();
        // set the field null
        amember.setMembNO(null);

        // Create the Amember, which fails.

        restAmemberMockMvc.perform(post("/api/amembers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amember)))
            .andExpect(status().isBadRequest());

        List<Amember> amemberList = amemberRepository.findAll();
        assertThat(amemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMembPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = amemberRepository.findAll().size();
        // set the field null
        amember.setMembPhone(null);

        // Create the Amember, which fails.

        restAmemberMockMvc.perform(post("/api/amembers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amember)))
            .andExpect(status().isBadRequest());

        List<Amember> amemberList = amemberRepository.findAll();
        assertThat(amemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMembQQIsRequired() throws Exception {
        int databaseSizeBeforeTest = amemberRepository.findAll().size();
        // set the field null
        amember.setMembQQ(null);

        // Create the Amember, which fails.

        restAmemberMockMvc.perform(post("/api/amembers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amember)))
            .andExpect(status().isBadRequest());

        List<Amember> amemberList = amemberRepository.findAll();
        assertThat(amemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAmembers() throws Exception {
        // Initialize the database
        amemberRepository.saveAndFlush(amember);

        // Get all the amemberList
        restAmemberMockMvc.perform(get("/api/amembers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amember.getId().intValue())))
            .andExpect(jsonPath("$.[*].membName").value(hasItem(DEFAULT_MEMB_NAME.toString())))
            .andExpect(jsonPath("$.[*].membNO").value(hasItem(DEFAULT_MEMB_NO.toString())))
            .andExpect(jsonPath("$.[*].membClass").value(hasItem(DEFAULT_MEMB_CLASS.toString())))
            .andExpect(jsonPath("$.[*].membPhone").value(hasItem(DEFAULT_MEMB_PHONE.toString())))
            .andExpect(jsonPath("$.[*].membQQ").value(hasItem(DEFAULT_MEMB_QQ.toString())))
            .andExpect(jsonPath("$.[*].membEmail").value(hasItem(DEFAULT_MEMB_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].membJoinDate").value(hasItem(DEFAULT_MEMB_JOIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].dormNum").value(hasItem(DEFAULT_DORM_NUM.toString())))
            .andExpect(jsonPath("$.[*].politicsStatus").value(hasItem(DEFAULT_POLITICS_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getAmember() throws Exception {
        // Initialize the database
        amemberRepository.saveAndFlush(amember);

        // Get the amember
        restAmemberMockMvc.perform(get("/api/amembers/{id}", amember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(amember.getId().intValue()))
            .andExpect(jsonPath("$.membName").value(DEFAULT_MEMB_NAME.toString()))
            .andExpect(jsonPath("$.membNO").value(DEFAULT_MEMB_NO.toString()))
            .andExpect(jsonPath("$.membClass").value(DEFAULT_MEMB_CLASS.toString()))
            .andExpect(jsonPath("$.membPhone").value(DEFAULT_MEMB_PHONE.toString()))
            .andExpect(jsonPath("$.membQQ").value(DEFAULT_MEMB_QQ.toString()))
            .andExpect(jsonPath("$.membEmail").value(DEFAULT_MEMB_EMAIL.toString()))
            .andExpect(jsonPath("$.membJoinDate").value(DEFAULT_MEMB_JOIN_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.dormNum").value(DEFAULT_DORM_NUM.toString()))
            .andExpect(jsonPath("$.politicsStatus").value(DEFAULT_POLITICS_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAmember() throws Exception {
        // Get the amember
        restAmemberMockMvc.perform(get("/api/amembers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAmember() throws Exception {
        // Initialize the database
        amemberRepository.saveAndFlush(amember);
        amemberSearchRepository.save(amember);
        int databaseSizeBeforeUpdate = amemberRepository.findAll().size();

        // Update the amember
        Amember updatedAmember = amemberRepository.findOne(amember.getId());
        updatedAmember
                .membName(UPDATED_MEMB_NAME)
                .membNO(UPDATED_MEMB_NO)
                .membClass(UPDATED_MEMB_CLASS)
                .membPhone(UPDATED_MEMB_PHONE)
                .membQQ(UPDATED_MEMB_QQ)
                .membEmail(UPDATED_MEMB_EMAIL)
                .membJoinDate(UPDATED_MEMB_JOIN_DATE)
                .gender(UPDATED_GENDER)
                .dormNum(UPDATED_DORM_NUM)
                .politicsStatus(UPDATED_POLITICS_STATUS);

        restAmemberMockMvc.perform(put("/api/amembers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAmember)))
            .andExpect(status().isOk());

        // Validate the Amember in the database
        List<Amember> amemberList = amemberRepository.findAll();
        assertThat(amemberList).hasSize(databaseSizeBeforeUpdate);
        Amember testAmember = amemberList.get(amemberList.size() - 1);
        assertThat(testAmember.getMembName()).isEqualTo(UPDATED_MEMB_NAME);
        assertThat(testAmember.getMembNO()).isEqualTo(UPDATED_MEMB_NO);
        assertThat(testAmember.getMembClass()).isEqualTo(UPDATED_MEMB_CLASS);
        assertThat(testAmember.getMembPhone()).isEqualTo(UPDATED_MEMB_PHONE);
        assertThat(testAmember.getMembQQ()).isEqualTo(UPDATED_MEMB_QQ);
        assertThat(testAmember.getMembEmail()).isEqualTo(UPDATED_MEMB_EMAIL);
        assertThat(testAmember.getMembJoinDate()).isEqualTo(UPDATED_MEMB_JOIN_DATE);
        assertThat(testAmember.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAmember.getDormNum()).isEqualTo(UPDATED_DORM_NUM);
        assertThat(testAmember.getPoliticsStatus()).isEqualTo(UPDATED_POLITICS_STATUS);

        // Validate the Amember in Elasticsearch
        Amember amemberEs = amemberSearchRepository.findOne(testAmember.getId());
        assertThat(amemberEs).isEqualToComparingFieldByField(testAmember);
    }

    @Test
    @Transactional
    public void updateNonExistingAmember() throws Exception {
        int databaseSizeBeforeUpdate = amemberRepository.findAll().size();

        // Create the Amember

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAmemberMockMvc.perform(put("/api/amembers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amember)))
            .andExpect(status().isCreated());

        // Validate the Amember in the database
        List<Amember> amemberList = amemberRepository.findAll();
        assertThat(amemberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAmember() throws Exception {
        // Initialize the database
        amemberRepository.saveAndFlush(amember);
        amemberSearchRepository.save(amember);
        int databaseSizeBeforeDelete = amemberRepository.findAll().size();

        // Get the amember
        restAmemberMockMvc.perform(delete("/api/amembers/{id}", amember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean amemberExistsInEs = amemberSearchRepository.exists(amember.getId());
        assertThat(amemberExistsInEs).isFalse();

        // Validate the database is empty
        List<Amember> amemberList = amemberRepository.findAll();
        assertThat(amemberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAmember() throws Exception {
        // Initialize the database
        amemberRepository.saveAndFlush(amember);
        amemberSearchRepository.save(amember);

        // Search the amember
        restAmemberMockMvc.perform(get("/api/_search/amembers?query=id:" + amember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amember.getId().intValue())))
            .andExpect(jsonPath("$.[*].membName").value(hasItem(DEFAULT_MEMB_NAME.toString())))
            .andExpect(jsonPath("$.[*].membNO").value(hasItem(DEFAULT_MEMB_NO.toString())))
            .andExpect(jsonPath("$.[*].membClass").value(hasItem(DEFAULT_MEMB_CLASS.toString())))
            .andExpect(jsonPath("$.[*].membPhone").value(hasItem(DEFAULT_MEMB_PHONE.toString())))
            .andExpect(jsonPath("$.[*].membQQ").value(hasItem(DEFAULT_MEMB_QQ.toString())))
            .andExpect(jsonPath("$.[*].membEmail").value(hasItem(DEFAULT_MEMB_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].membJoinDate").value(hasItem(DEFAULT_MEMB_JOIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].dormNum").value(hasItem(DEFAULT_DORM_NUM.toString())))
            .andExpect(jsonPath("$.[*].politicsStatus").value(hasItem(DEFAULT_POLITICS_STATUS.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Amember.class);
    }
}
