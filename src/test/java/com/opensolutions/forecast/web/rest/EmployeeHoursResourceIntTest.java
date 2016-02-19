package com.opensolutions.forecast.web.rest;

import com.opensolutions.forecast.Application;
import com.opensolutions.forecast.domain.EmployeeHours;
import com.opensolutions.forecast.repository.EmployeeHoursRepository;
import com.opensolutions.forecast.service.EmployeeHoursService;

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
 * Test class for the EmployeeHoursResource REST controller.
 *
 * @see EmployeeHoursResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeHoursResourceIntTest {


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
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    private static final LocalDate DEFAULT_LAST_CHANGED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_CHANGED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LAST_CHANGED_BY = "AAAAA";
    private static final String UPDATED_LAST_CHANGED_BY = "BBBBB";

    @Inject
    private EmployeeHoursRepository employeeHoursRepository;

    @Inject
    private EmployeeHoursService employeeHoursService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeHoursMockMvc;

    private EmployeeHours employeeHours;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeHoursResource employeeHoursResource = new EmployeeHoursResource();
        ReflectionTestUtils.setField(employeeHoursResource, "employeeHoursService", employeeHoursService);
        this.restEmployeeHoursMockMvc = MockMvcBuilders.standaloneSetup(employeeHoursResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employeeHours = new EmployeeHours();
        employeeHours.setWeek1(DEFAULT_WEEK1);
        employeeHours.setWeek2(DEFAULT_WEEK2);
        employeeHours.setWeek3(DEFAULT_WEEK3);
        employeeHours.setWeek4(DEFAULT_WEEK4);
        employeeHours.setWeek5(DEFAULT_WEEK5);
        employeeHours.setCreatedDate(DEFAULT_CREATED_DATE);
        employeeHours.setForecastDate(DEFAULT_FORECAST_DATE);
        employeeHours.setType(DEFAULT_TYPE);
        employeeHours.setLastChangedDate(DEFAULT_LAST_CHANGED_DATE);
        employeeHours.setLastChangedBy(DEFAULT_LAST_CHANGED_BY);
    }

    @Test
    @Transactional
    public void createEmployeeHours() throws Exception {
        int databaseSizeBeforeCreate = employeeHoursRepository.findAll().size();

        // Create the EmployeeHours

        restEmployeeHoursMockMvc.perform(post("/api/employeeHourss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeHours)))
                .andExpect(status().isCreated());

        // Validate the EmployeeHours in the database
        List<EmployeeHours> employeeHourss = employeeHoursRepository.findAll();
        assertThat(employeeHourss).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeHours testEmployeeHours = employeeHourss.get(employeeHourss.size() - 1);
        assertThat(testEmployeeHours.getWeek1()).isEqualTo(DEFAULT_WEEK1);
        assertThat(testEmployeeHours.getWeek2()).isEqualTo(DEFAULT_WEEK2);
        assertThat(testEmployeeHours.getWeek3()).isEqualTo(DEFAULT_WEEK3);
        assertThat(testEmployeeHours.getWeek4()).isEqualTo(DEFAULT_WEEK4);
        assertThat(testEmployeeHours.getWeek5()).isEqualTo(DEFAULT_WEEK5);
        assertThat(testEmployeeHours.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEmployeeHours.getForecastDate()).isEqualTo(DEFAULT_FORECAST_DATE);
        assertThat(testEmployeeHours.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEmployeeHours.getLastChangedDate()).isEqualTo(DEFAULT_LAST_CHANGED_DATE);
        assertThat(testEmployeeHours.getLastChangedBy()).isEqualTo(DEFAULT_LAST_CHANGED_BY);
    }

    @Test
    @Transactional
    public void getAllEmployeeHourss() throws Exception {
        // Initialize the database
        employeeHoursRepository.saveAndFlush(employeeHours);

        // Get all the employeeHourss
        restEmployeeHoursMockMvc.perform(get("/api/employeeHourss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeHours.getId().intValue())))
                .andExpect(jsonPath("$.[*].week1").value(hasItem(DEFAULT_WEEK1)))
                .andExpect(jsonPath("$.[*].week2").value(hasItem(DEFAULT_WEEK2)))
                .andExpect(jsonPath("$.[*].week3").value(hasItem(DEFAULT_WEEK3)))
                .andExpect(jsonPath("$.[*].week4").value(hasItem(DEFAULT_WEEK4)))
                .andExpect(jsonPath("$.[*].week5").value(hasItem(DEFAULT_WEEK5)))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].forecastDate").value(hasItem(DEFAULT_FORECAST_DATE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].lastChangedDate").value(hasItem(DEFAULT_LAST_CHANGED_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastChangedBy").value(hasItem(DEFAULT_LAST_CHANGED_BY.toString())));
    }

    @Test
    @Transactional
    public void getEmployeeHours() throws Exception {
        // Initialize the database
        employeeHoursRepository.saveAndFlush(employeeHours);

        // Get the employeeHours
        restEmployeeHoursMockMvc.perform(get("/api/employeeHourss/{id}", employeeHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employeeHours.getId().intValue()))
            .andExpect(jsonPath("$.week1").value(DEFAULT_WEEK1))
            .andExpect(jsonPath("$.week2").value(DEFAULT_WEEK2))
            .andExpect(jsonPath("$.week3").value(DEFAULT_WEEK3))
            .andExpect(jsonPath("$.week4").value(DEFAULT_WEEK4))
            .andExpect(jsonPath("$.week5").value(DEFAULT_WEEK5))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.forecastDate").value(DEFAULT_FORECAST_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.lastChangedDate").value(DEFAULT_LAST_CHANGED_DATE.toString()))
            .andExpect(jsonPath("$.lastChangedBy").value(DEFAULT_LAST_CHANGED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeHours() throws Exception {
        // Get the employeeHours
        restEmployeeHoursMockMvc.perform(get("/api/employeeHourss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeHours() throws Exception {
        // Initialize the database
        employeeHoursRepository.saveAndFlush(employeeHours);

		int databaseSizeBeforeUpdate = employeeHoursRepository.findAll().size();

        // Update the employeeHours
        employeeHours.setWeek1(UPDATED_WEEK1);
        employeeHours.setWeek2(UPDATED_WEEK2);
        employeeHours.setWeek3(UPDATED_WEEK3);
        employeeHours.setWeek4(UPDATED_WEEK4);
        employeeHours.setWeek5(UPDATED_WEEK5);
        employeeHours.setCreatedDate(UPDATED_CREATED_DATE);
        employeeHours.setForecastDate(UPDATED_FORECAST_DATE);
        employeeHours.setType(UPDATED_TYPE);
        employeeHours.setLastChangedDate(UPDATED_LAST_CHANGED_DATE);
        employeeHours.setLastChangedBy(UPDATED_LAST_CHANGED_BY);

        restEmployeeHoursMockMvc.perform(put("/api/employeeHourss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeHours)))
                .andExpect(status().isOk());

        // Validate the EmployeeHours in the database
        List<EmployeeHours> employeeHourss = employeeHoursRepository.findAll();
        assertThat(employeeHourss).hasSize(databaseSizeBeforeUpdate);
        EmployeeHours testEmployeeHours = employeeHourss.get(employeeHourss.size() - 1);
        assertThat(testEmployeeHours.getWeek1()).isEqualTo(UPDATED_WEEK1);
        assertThat(testEmployeeHours.getWeek2()).isEqualTo(UPDATED_WEEK2);
        assertThat(testEmployeeHours.getWeek3()).isEqualTo(UPDATED_WEEK3);
        assertThat(testEmployeeHours.getWeek4()).isEqualTo(UPDATED_WEEK4);
        assertThat(testEmployeeHours.getWeek5()).isEqualTo(UPDATED_WEEK5);
        assertThat(testEmployeeHours.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEmployeeHours.getForecastDate()).isEqualTo(UPDATED_FORECAST_DATE);
        assertThat(testEmployeeHours.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEmployeeHours.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
        assertThat(testEmployeeHours.getLastChangedBy()).isEqualTo(UPDATED_LAST_CHANGED_BY);
    }

    @Test
    @Transactional
    public void deleteEmployeeHours() throws Exception {
        // Initialize the database
        employeeHoursRepository.saveAndFlush(employeeHours);

		int databaseSizeBeforeDelete = employeeHoursRepository.findAll().size();

        // Get the employeeHours
        restEmployeeHoursMockMvc.perform(delete("/api/employeeHourss/{id}", employeeHours.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeHours> employeeHourss = employeeHoursRepository.findAll();
        assertThat(employeeHourss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
