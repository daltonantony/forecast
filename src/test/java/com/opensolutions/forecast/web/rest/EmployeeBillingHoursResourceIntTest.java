package com.opensolutions.forecast.web.rest;

import com.opensolutions.forecast.Application;
import com.opensolutions.forecast.domain.EmployeeBillingHours;
import com.opensolutions.forecast.repository.EmployeeBillingHoursRepository;
import com.opensolutions.forecast.service.EmployeeBillingHoursService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EmployeeBillingHoursResource REST controller.
 *
 * @see EmployeeBillingHoursResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeBillingHoursResourceIntTest {


    private static final Integer DEFAULT_WEEK1 = 1;
    private static final Integer UPDATED_WEEK1 = 2;

    private static final Integer DEFAULT_WEEK2 = 1;
    private static final Integer UPDATED_WEEK2 = 2;

    private static final Integer DEFAULT_WEEK3 = 1;
    private static final Integer UPDATED_WEEK3 = 2;

    private static final Integer DEFAULT_WEEK4 = 1;
    private static final Integer UPDATED_WEEK4 = 2;

    private static final Integer DEFAULT_WEEK5 = 1;
    private static final Integer UPDATED_WEEK5 = 2;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FORECAST_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FORECAST_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_CHANGED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_CHANGED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LAST_CHANGED_BY = "AAAAA";
    private static final String UPDATED_LAST_CHANGED_BY = "BBBBB";

    @Inject
    private EmployeeBillingHoursRepository employeeBillingHoursRepository;

    @Inject
    private EmployeeBillingHoursService employeeBillingHoursService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeBillingHoursMockMvc;

    private EmployeeBillingHours employeeBillingHours;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeBillingHoursResource employeeBillingHoursResource = new EmployeeBillingHoursResource();
        ReflectionTestUtils.setField(employeeBillingHoursResource, "employeeBillingHoursService", employeeBillingHoursService);
        this.restEmployeeBillingHoursMockMvc = MockMvcBuilders.standaloneSetup(employeeBillingHoursResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employeeBillingHours = new EmployeeBillingHours();
        employeeBillingHours.setWeek1(DEFAULT_WEEK1);
        employeeBillingHours.setWeek2(DEFAULT_WEEK2);
        employeeBillingHours.setWeek3(DEFAULT_WEEK3);
        employeeBillingHours.setWeek4(DEFAULT_WEEK4);
        employeeBillingHours.setWeek5(DEFAULT_WEEK5);
        employeeBillingHours.setCreatedDate(DEFAULT_CREATED_DATE);
        employeeBillingHours.setForecastDate(DEFAULT_FORECAST_DATE);
        employeeBillingHours.setLastChangedDate(DEFAULT_LAST_CHANGED_DATE);
        employeeBillingHours.setLastChangedBy(DEFAULT_LAST_CHANGED_BY);
    }

    @Test
    @Transactional
    public void createEmployeeBillingHours() throws Exception {
        int databaseSizeBeforeCreate = employeeBillingHoursRepository.findAll().size();

        // Create the EmployeeBillingHours

        restEmployeeBillingHoursMockMvc.perform(post("/api/employeeBillingHourss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeBillingHours)))
                .andExpect(status().isCreated());

        // Validate the EmployeeBillingHours in the database
        List<EmployeeBillingHours> employeeBillingHourss = employeeBillingHoursRepository.findAll();
        assertThat(employeeBillingHourss).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeBillingHours testEmployeeBillingHours = employeeBillingHourss.get(employeeBillingHourss.size() - 1);
        assertThat(testEmployeeBillingHours.getWeek1()).isEqualTo(DEFAULT_WEEK1);
        assertThat(testEmployeeBillingHours.getWeek2()).isEqualTo(DEFAULT_WEEK2);
        assertThat(testEmployeeBillingHours.getWeek3()).isEqualTo(DEFAULT_WEEK3);
        assertThat(testEmployeeBillingHours.getWeek4()).isEqualTo(DEFAULT_WEEK4);
        assertThat(testEmployeeBillingHours.getWeek5()).isEqualTo(DEFAULT_WEEK5);
        assertThat(testEmployeeBillingHours.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEmployeeBillingHours.getForecastDate()).isEqualTo(DEFAULT_FORECAST_DATE);
        assertThat(testEmployeeBillingHours.getLastChangedDate()).isEqualTo(DEFAULT_LAST_CHANGED_DATE);
        assertThat(testEmployeeBillingHours.getLastChangedBy()).isEqualTo(DEFAULT_LAST_CHANGED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployeeBillingHourss() throws Exception {
        // Initialize the database
        employeeBillingHoursRepository.saveAndFlush(employeeBillingHours);

        // Get all the employeeBillingHourss
        restEmployeeBillingHoursMockMvc.perform(get("/api/employeeBillingHourss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeBillingHours.getId().intValue())))
                .andExpect(jsonPath("$.[*].week1").value(hasItem(DEFAULT_WEEK1)))
                .andExpect(jsonPath("$.[*].week2").value(hasItem(DEFAULT_WEEK2)))
                .andExpect(jsonPath("$.[*].week3").value(hasItem(DEFAULT_WEEK3)))
                .andExpect(jsonPath("$.[*].week4").value(hasItem(DEFAULT_WEEK4)))
                .andExpect(jsonPath("$.[*].week5").value(hasItem(DEFAULT_WEEK5)))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].forecastDate").value(hasItem(DEFAULT_FORECAST_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastChangedDate").value(hasItem(DEFAULT_LAST_CHANGED_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastChangedBy").value(hasItem(DEFAULT_LAST_CHANGED_BY.toString())));
    }

    @Test
    @Transactional
    public void getEmployeeBillingHours() throws Exception {
        // Initialize the database
        employeeBillingHoursRepository.saveAndFlush(employeeBillingHours);

        // Get the employeeBillingHours
        restEmployeeBillingHoursMockMvc.perform(get("/api/employeeBillingHourss/{id}", employeeBillingHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employeeBillingHours.getId().intValue()))
            .andExpect(jsonPath("$.week1").value(DEFAULT_WEEK1))
            .andExpect(jsonPath("$.week2").value(DEFAULT_WEEK2))
            .andExpect(jsonPath("$.week3").value(DEFAULT_WEEK3))
            .andExpect(jsonPath("$.week4").value(DEFAULT_WEEK4))
            .andExpect(jsonPath("$.week5").value(DEFAULT_WEEK5))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.forecastDate").value(DEFAULT_FORECAST_DATE.toString()))
            .andExpect(jsonPath("$.lastChangedDate").value(DEFAULT_LAST_CHANGED_DATE.toString()))
            .andExpect(jsonPath("$.lastChangedBy").value(DEFAULT_LAST_CHANGED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeBillingHours() throws Exception {
        // Get the employeeBillingHours
        restEmployeeBillingHoursMockMvc.perform(get("/api/employeeBillingHourss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeBillingHours() throws Exception {
        // Initialize the database
        employeeBillingHoursRepository.saveAndFlush(employeeBillingHours);

		int databaseSizeBeforeUpdate = employeeBillingHoursRepository.findAll().size();

        // Update the employeeBillingHours
        employeeBillingHours.setWeek1(UPDATED_WEEK1);
        employeeBillingHours.setWeek2(UPDATED_WEEK2);
        employeeBillingHours.setWeek3(UPDATED_WEEK3);
        employeeBillingHours.setWeek4(UPDATED_WEEK4);
        employeeBillingHours.setWeek5(UPDATED_WEEK5);
        employeeBillingHours.setCreatedDate(UPDATED_CREATED_DATE);
        employeeBillingHours.setForecastDate(UPDATED_FORECAST_DATE);
        employeeBillingHours.setLastChangedDate(UPDATED_LAST_CHANGED_DATE);
        employeeBillingHours.setLastChangedBy(UPDATED_LAST_CHANGED_BY);

        restEmployeeBillingHoursMockMvc.perform(put("/api/employeeBillingHourss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeBillingHours)))
                .andExpect(status().isOk());

        // Validate the EmployeeBillingHours in the database
        List<EmployeeBillingHours> employeeBillingHourss = employeeBillingHoursRepository.findAll();
        assertThat(employeeBillingHourss).hasSize(databaseSizeBeforeUpdate);
        EmployeeBillingHours testEmployeeBillingHours = employeeBillingHourss.get(employeeBillingHourss.size() - 1);
        assertThat(testEmployeeBillingHours.getWeek1()).isEqualTo(UPDATED_WEEK1);
        assertThat(testEmployeeBillingHours.getWeek2()).isEqualTo(UPDATED_WEEK2);
        assertThat(testEmployeeBillingHours.getWeek3()).isEqualTo(UPDATED_WEEK3);
        assertThat(testEmployeeBillingHours.getWeek4()).isEqualTo(UPDATED_WEEK4);
        assertThat(testEmployeeBillingHours.getWeek5()).isEqualTo(UPDATED_WEEK5);
        assertThat(testEmployeeBillingHours.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEmployeeBillingHours.getForecastDate()).isEqualTo(UPDATED_FORECAST_DATE);
        assertThat(testEmployeeBillingHours.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
        assertThat(testEmployeeBillingHours.getLastChangedBy()).isEqualTo(UPDATED_LAST_CHANGED_BY);
    }

    @Test
    @Transactional
    public void deleteEmployeeBillingHours() throws Exception {
        // Initialize the database
        employeeBillingHoursRepository.saveAndFlush(employeeBillingHours);

		int databaseSizeBeforeDelete = employeeBillingHoursRepository.findAll().size();

        // Get the employeeBillingHours
        restEmployeeBillingHoursMockMvc.perform(delete("/api/employeeBillingHourss/{id}", employeeBillingHours.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeBillingHours> employeeBillingHourss = employeeBillingHoursRepository.findAll();
        assertThat(employeeBillingHourss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
