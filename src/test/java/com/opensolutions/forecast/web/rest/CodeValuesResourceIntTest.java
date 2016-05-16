package com.opensolutions.forecast.web.rest;

import com.opensolutions.forecast.Application;
import com.opensolutions.forecast.domain.CodeValues;
import com.opensolutions.forecast.repository.CodeValuesRepository;
import com.opensolutions.forecast.service.CodeValuesService;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CodeValuesResource REST controller.
 *
 * @see CodeValuesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Ignore
public class CodeValuesResourceIntTest {

    private static final String DEFAULT_CODE_TYPE = "AAAAA";
    private static final String UPDATED_CODE_TYPE = "BBBBB";
    private static final String DEFAULT_CODE_VALUE = "AAAAA";
    private static final String UPDATED_CODE_VALUE = "BBBBB";

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LAST_CHANGED_BY = "AAAAA";
    private static final String UPDATED_LAST_CHANGED_BY = "BBBBB";

    private static final LocalDate DEFAULT_LAST_CHANGED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_CHANGED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private CodeValuesRepository codeValuesRepository;

    @Inject
    private CodeValuesService codeValuesService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCodeValuesMockMvc;

    private CodeValues codeValues;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CodeValuesResource codeValuesResource = new CodeValuesResource();
        ReflectionTestUtils.setField(codeValuesResource, "codeValuesService", codeValuesService);
        this.restCodeValuesMockMvc = MockMvcBuilders.standaloneSetup(codeValuesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        codeValues = new CodeValues();
        codeValues.setCodeType(DEFAULT_CODE_TYPE);
        codeValues.setCodeValue(DEFAULT_CODE_VALUE);
        codeValues.setEffectiveDate(DEFAULT_EFFECTIVE_DATE);
        codeValues.setExpiryDate(DEFAULT_EXPIRY_DATE);
        codeValues.setLastChangedBy(DEFAULT_LAST_CHANGED_BY);
        codeValues.setLastChangedDate(DEFAULT_LAST_CHANGED_DATE);
    }

    @Test
    @Transactional
    public void createCodeValues() throws Exception {
        int databaseSizeBeforeCreate = codeValuesRepository.findAll().size();

        // mock the security context for the logged in user name
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        // Create the CodeValues

        restCodeValuesMockMvc.perform(post("/api/codeValuess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(codeValues)))
                .andExpect(status().isCreated());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuess = codeValuesRepository.findAll();
        assertThat(codeValuess).hasSize(databaseSizeBeforeCreate + 1);
        CodeValues testCodeValues = codeValuess.get(codeValuess.size() - 1);
        assertThat(testCodeValues.getCodeType()).isEqualTo(DEFAULT_CODE_TYPE);
        assertThat(testCodeValues.getCodeValue()).isEqualTo(DEFAULT_CODE_VALUE);
        assertThat(testCodeValues.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testCodeValues.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testCodeValues.getLastChangedBy()).isEqualTo("admin");
        assertThat(testCodeValues.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
    }

    @Test
    @Transactional
    public void getAllCodeValuess() throws Exception {
        // Initialize the database
        codeValuesRepository.saveAndFlush(codeValues);

        // Get all the codeValuess
        restCodeValuesMockMvc.perform(get("/api/codeValuess?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(codeValues.getId().intValue())))
                .andExpect(jsonPath("$.[*].codeType").value(hasItem(DEFAULT_CODE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].codeValue").value(hasItem(DEFAULT_CODE_VALUE.toString())))
                .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastChangedBy").value(hasItem(DEFAULT_LAST_CHANGED_BY.toString())))
                .andExpect(jsonPath("$.[*].lastChangedDate").value(hasItem(DEFAULT_LAST_CHANGED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getCodeValues() throws Exception {
        // Initialize the database
        codeValuesRepository.saveAndFlush(codeValues);

        // Get the codeValues
        restCodeValuesMockMvc.perform(get("/api/codeValuess/{id}", codeValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(codeValues.getId().intValue()))
            .andExpect(jsonPath("$.codeType").value(DEFAULT_CODE_TYPE.toString()))
            .andExpect(jsonPath("$.codeValue").value(DEFAULT_CODE_VALUE.toString()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.lastChangedBy").value(DEFAULT_LAST_CHANGED_BY.toString()))
            .andExpect(jsonPath("$.lastChangedDate").value(DEFAULT_LAST_CHANGED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCodeValues() throws Exception {
        // Get the codeValues
        restCodeValuesMockMvc.perform(get("/api/codeValuess/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCodeValues() throws Exception {
        // Initialize the database
        codeValuesRepository.saveAndFlush(codeValues);

		int databaseSizeBeforeUpdate = codeValuesRepository.findAll().size();

        // mock the security context for the logged in user name
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        // Update the codeValues
        codeValues.setCodeType(UPDATED_CODE_TYPE);
        codeValues.setCodeValue(UPDATED_CODE_VALUE);
        codeValues.setEffectiveDate(UPDATED_EFFECTIVE_DATE);
        codeValues.setExpiryDate(UPDATED_EXPIRY_DATE);
        codeValues.setLastChangedBy(UPDATED_LAST_CHANGED_BY);
        codeValues.setLastChangedDate(UPDATED_LAST_CHANGED_DATE);

        restCodeValuesMockMvc.perform(put("/api/codeValuess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(codeValues)))
                .andExpect(status().isOk());

        // Validate the CodeValues in the database
        List<CodeValues> codeValuess = codeValuesRepository.findAll();
        assertThat(codeValuess).hasSize(databaseSizeBeforeUpdate);
        CodeValues testCodeValues = codeValuess.get(codeValuess.size() - 1);
        assertThat(testCodeValues.getCodeType()).isEqualTo(UPDATED_CODE_TYPE);
        assertThat(testCodeValues.getCodeValue()).isEqualTo(UPDATED_CODE_VALUE);
        assertThat(testCodeValues.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testCodeValues.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testCodeValues.getLastChangedBy()).isEqualTo("admin");
        assertThat(testCodeValues.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
    }

    @Test
    @Transactional
    public void deleteCodeValues() throws Exception {
        // Initialize the database
        codeValuesRepository.saveAndFlush(codeValues);

		int databaseSizeBeforeDelete = codeValuesRepository.findAll().size();

        // Get the codeValues
        restCodeValuesMockMvc.perform(delete("/api/codeValuess/{id}", codeValues.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CodeValues> codeValuess = codeValuesRepository.findAll();
        assertThat(codeValuess).hasSize(databaseSizeBeforeDelete - 1);
    }
}
