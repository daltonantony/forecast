package com.opensolutions.forecast.web.rest;

import com.opensolutions.forecast.Application;
import com.opensolutions.forecast.domain.ROLE_TARIFF;
import com.opensolutions.forecast.repository.ROLE_TARIFFRepository;
import com.opensolutions.forecast.service.ROLE_TARIFFService;

import org.junit.Before;
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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ROLE_TARIFFResource REST controller.
 *
 * @see ROLE_TARIFFResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ROLE_TARIFFResourceIntTest {

    private static final String DEFAULT_ROLE = "AAAAA";
    private static final String UPDATED_ROLE = "BBBBB";

    private static final BigDecimal DEFAULT_TARIFF = new BigDecimal(1);
    private static final BigDecimal UPDATED_TARIFF = new BigDecimal(2);
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_CHANGED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_CHANGED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LAST_CHANGED_BY = "AAAAA";
    private static final String UPDATED_LAST_CHANGED_BY = "BBBBB";

    @Inject
    private ROLE_TARIFFRepository rOLE_TARIFFRepository;

    @Inject
    private ROLE_TARIFFService rOLE_TARIFFService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restROLE_TARIFFMockMvc;

    private ROLE_TARIFF rOLE_TARIFF;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ROLE_TARIFFResource rOLE_TARIFFResource = new ROLE_TARIFFResource();
        ReflectionTestUtils.setField(rOLE_TARIFFResource, "rOLE_TARIFFService", rOLE_TARIFFService);
        this.restROLE_TARIFFMockMvc = MockMvcBuilders.standaloneSetup(rOLE_TARIFFResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        rOLE_TARIFF = new ROLE_TARIFF();
        rOLE_TARIFF.setRole(DEFAULT_ROLE);
        rOLE_TARIFF.setTariff(DEFAULT_TARIFF);
        rOLE_TARIFF.setLocation(DEFAULT_LOCATION);
        rOLE_TARIFF.setEffectiveDate(DEFAULT_EFFECTIVE_DATE);
        rOLE_TARIFF.setExpiryDate(DEFAULT_EXPIRY_DATE);
        rOLE_TARIFF.setLastChangedDate(DEFAULT_LAST_CHANGED_DATE);
        rOLE_TARIFF.setLastChangedBy(DEFAULT_LAST_CHANGED_BY);
    }

    @Test
    @Transactional
    public void createROLE_TARIFF() throws Exception {
        int databaseSizeBeforeCreate = rOLE_TARIFFRepository.findAll().size();
        
        // mock the security context for the logged in user name
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        // Create the ROLE_TARIFF

        restROLE_TARIFFMockMvc.perform(post("/api/rOLE_TARIFFs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rOLE_TARIFF)))
                .andExpect(status().isCreated());

        // Validate the ROLE_TARIFF in the database
        List<ROLE_TARIFF> rOLE_TARIFFs = rOLE_TARIFFRepository.findAll();
        assertThat(rOLE_TARIFFs).hasSize(databaseSizeBeforeCreate + 1);
        ROLE_TARIFF testROLE_TARIFF = rOLE_TARIFFs.get(rOLE_TARIFFs.size() - 1);
        assertThat(testROLE_TARIFF.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testROLE_TARIFF.getTariff()).isEqualTo(DEFAULT_TARIFF);
        assertThat(testROLE_TARIFF.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testROLE_TARIFF.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testROLE_TARIFF.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testROLE_TARIFF.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
        assertThat(testROLE_TARIFF.getLastChangedBy()).isEqualTo("admin");
    }

    @Test
    @Transactional
    public void getAllROLE_TARIFFs() throws Exception {
        // Initialize the database
        rOLE_TARIFFRepository.saveAndFlush(rOLE_TARIFF);

        // Get all the rOLE_TARIFFs
        restROLE_TARIFFMockMvc.perform(get("/api/rOLE_TARIFFs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(rOLE_TARIFF.getId().intValue())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
                .andExpect(jsonPath("$.[*].tariff").value(hasItem(DEFAULT_TARIFF.intValue())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastChangedDate").value(hasItem(DEFAULT_LAST_CHANGED_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastChangedBy").value(hasItem(DEFAULT_LAST_CHANGED_BY.toString())));
    }

    @Test
    @Transactional
    public void getROLE_TARIFF() throws Exception {
        // Initialize the database
        rOLE_TARIFFRepository.saveAndFlush(rOLE_TARIFF);

        // Get the rOLE_TARIFF
        restROLE_TARIFFMockMvc.perform(get("/api/rOLE_TARIFFs/{id}", rOLE_TARIFF.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(rOLE_TARIFF.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.tariff").value(DEFAULT_TARIFF.intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.lastChangedDate").value(DEFAULT_LAST_CHANGED_DATE.toString()))
            .andExpect(jsonPath("$.lastChangedBy").value(DEFAULT_LAST_CHANGED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingROLE_TARIFF() throws Exception {
        // Get the rOLE_TARIFF
        restROLE_TARIFFMockMvc.perform(get("/api/rOLE_TARIFFs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateROLE_TARIFF() throws Exception {
        // Initialize the database
        rOLE_TARIFFRepository.saveAndFlush(rOLE_TARIFF);

		int databaseSizeBeforeUpdate = rOLE_TARIFFRepository.findAll().size();
        
        // mock the security context for the logged in user name
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        // Update the rOLE_TARIFF
        rOLE_TARIFF.setRole(UPDATED_ROLE);
        rOLE_TARIFF.setTariff(UPDATED_TARIFF);
        rOLE_TARIFF.setLocation(UPDATED_LOCATION);
        rOLE_TARIFF.setEffectiveDate(UPDATED_EFFECTIVE_DATE);
        rOLE_TARIFF.setExpiryDate(UPDATED_EXPIRY_DATE);
        rOLE_TARIFF.setLastChangedDate(UPDATED_LAST_CHANGED_DATE);
        rOLE_TARIFF.setLastChangedBy(UPDATED_LAST_CHANGED_BY);

        restROLE_TARIFFMockMvc.perform(put("/api/rOLE_TARIFFs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rOLE_TARIFF)))
                .andExpect(status().isOk());

        // Validate the ROLE_TARIFF in the database
        List<ROLE_TARIFF> rOLE_TARIFFs = rOLE_TARIFFRepository.findAll();
        assertThat(rOLE_TARIFFs).hasSize(databaseSizeBeforeUpdate);
        ROLE_TARIFF testROLE_TARIFF = rOLE_TARIFFs.get(rOLE_TARIFFs.size() - 1);
        assertThat(testROLE_TARIFF.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testROLE_TARIFF.getTariff()).isEqualTo(UPDATED_TARIFF);
        assertThat(testROLE_TARIFF.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testROLE_TARIFF.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testROLE_TARIFF.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testROLE_TARIFF.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
        assertThat(testROLE_TARIFF.getLastChangedBy()).isEqualTo("admin");
    }

    @Test
    @Transactional
    public void deleteROLE_TARIFF() throws Exception {
        // Initialize the database
        rOLE_TARIFFRepository.saveAndFlush(rOLE_TARIFF);

		int databaseSizeBeforeDelete = rOLE_TARIFFRepository.findAll().size();

        // Get the rOLE_TARIFF
        restROLE_TARIFFMockMvc.perform(delete("/api/rOLE_TARIFFs/{id}", rOLE_TARIFF.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ROLE_TARIFF> rOLE_TARIFFs = rOLE_TARIFFRepository.findAll();
        assertThat(rOLE_TARIFFs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
