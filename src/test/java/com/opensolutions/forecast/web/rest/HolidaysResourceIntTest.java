package com.opensolutions.forecast.web.rest;

import com.opensolutions.forecast.Application;
import com.opensolutions.forecast.domain.Holidays;
import com.opensolutions.forecast.repository.HolidaysRepository;
import com.opensolutions.forecast.service.HolidaysService;

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
 * Test class for the HolidaysResource REST controller.
 *
 * @see HolidaysResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HolidaysResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";
    private static final String DEFAULT_LAST_CHANGED_BY = "AAAAA";
    private static final String UPDATED_LAST_CHANGED_BY = "BBBBB";

    private static final LocalDate DEFAULT_LAST_CHANGED_DT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_CHANGED_DT = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private HolidaysRepository holidaysRepository;

    @Inject
    private HolidaysService holidaysService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHolidaysMockMvc;

    private Holidays holidays;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HolidaysResource holidaysResource = new HolidaysResource();
        ReflectionTestUtils.setField(holidaysResource, "holidaysService", holidaysService);
        this.restHolidaysMockMvc = MockMvcBuilders.standaloneSetup(holidaysResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        holidays = new Holidays();
        holidays.setName(DEFAULT_NAME);
        holidays.setStartDate(DEFAULT_START_DATE);
        holidays.setEndDate(DEFAULT_END_DATE);
        holidays.setLocation(DEFAULT_LOCATION);
        holidays.setLastChangedBy(DEFAULT_LAST_CHANGED_BY);
        holidays.setLastChangedDt(DEFAULT_LAST_CHANGED_DT);
    }

    @Test
    @Transactional
    public void createHolidays() throws Exception {
        int databaseSizeBeforeCreate = holidaysRepository.findAll().size();

        // Create the Holidays

        restHolidaysMockMvc.perform(post("/api/holidayss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holidays)))
                .andExpect(status().isCreated());

        // Validate the Holidays in the database
        List<Holidays> holidayss = holidaysRepository.findAll();
        assertThat(holidayss).hasSize(databaseSizeBeforeCreate + 1);
        Holidays testHolidays = holidayss.get(holidayss.size() - 1);
        assertThat(testHolidays.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHolidays.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testHolidays.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testHolidays.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testHolidays.getLastChangedBy()).isEqualTo(DEFAULT_LAST_CHANGED_BY);
        assertThat(testHolidays.getLastChangedDt()).isEqualTo(DEFAULT_LAST_CHANGED_DT);
    }

    @Test
    @Transactional
    public void getAllHolidayss() throws Exception {
        // Initialize the database
        holidaysRepository.saveAndFlush(holidays);

        // Get all the holidayss
        restHolidaysMockMvc.perform(get("/api/holidayss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(holidays.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].lastChangedBy").value(hasItem(DEFAULT_LAST_CHANGED_BY.toString())))
                .andExpect(jsonPath("$.[*].lastChangedDt").value(hasItem(DEFAULT_LAST_CHANGED_DT.toString())));
    }

    @Test
    @Transactional
    public void getHolidays() throws Exception {
        // Initialize the database
        holidaysRepository.saveAndFlush(holidays);

        // Get the holidays
        restHolidaysMockMvc.perform(get("/api/holidayss/{id}", holidays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(holidays.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.lastChangedBy").value(DEFAULT_LAST_CHANGED_BY.toString()))
            .andExpect(jsonPath("$.lastChangedDt").value(DEFAULT_LAST_CHANGED_DT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHolidays() throws Exception {
        // Get the holidays
        restHolidaysMockMvc.perform(get("/api/holidayss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHolidays() throws Exception {
        // Initialize the database
        holidaysRepository.saveAndFlush(holidays);

		int databaseSizeBeforeUpdate = holidaysRepository.findAll().size();

        // Update the holidays
        holidays.setName(UPDATED_NAME);
        holidays.setStartDate(UPDATED_START_DATE);
        holidays.setEndDate(UPDATED_END_DATE);
        holidays.setLocation(UPDATED_LOCATION);
        holidays.setLastChangedBy(UPDATED_LAST_CHANGED_BY);
        holidays.setLastChangedDt(UPDATED_LAST_CHANGED_DT);

        restHolidaysMockMvc.perform(put("/api/holidayss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holidays)))
                .andExpect(status().isOk());

        // Validate the Holidays in the database
        List<Holidays> holidayss = holidaysRepository.findAll();
        assertThat(holidayss).hasSize(databaseSizeBeforeUpdate);
        Holidays testHolidays = holidayss.get(holidayss.size() - 1);
        assertThat(testHolidays.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHolidays.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testHolidays.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testHolidays.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testHolidays.getLastChangedBy()).isEqualTo(UPDATED_LAST_CHANGED_BY);
        assertThat(testHolidays.getLastChangedDt()).isEqualTo(UPDATED_LAST_CHANGED_DT);
    }

    @Test
    @Transactional
    public void deleteHolidays() throws Exception {
        // Initialize the database
        holidaysRepository.saveAndFlush(holidays);

		int databaseSizeBeforeDelete = holidaysRepository.findAll().size();

        // Get the holidays
        restHolidaysMockMvc.perform(delete("/api/holidayss/{id}", holidays.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Holidays> holidayss = holidaysRepository.findAll();
        assertThat(holidayss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
