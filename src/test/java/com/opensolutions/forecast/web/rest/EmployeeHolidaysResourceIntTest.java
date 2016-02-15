package com.opensolutions.forecast.web.rest;

import com.opensolutions.forecast.Application;
import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.domain.EmployeeBillingHours;
import com.opensolutions.forecast.domain.EmployeeHolidays;
import com.opensolutions.forecast.repository.EmployeeBillingHoursRepository;
import com.opensolutions.forecast.repository.EmployeeHolidaysRepository;
import com.opensolutions.forecast.repository.EmployeeRepository;
import com.opensolutions.forecast.service.EmployeeHolidaysService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EmployeeHolidaysResource REST controller.
 *
 * @see EmployeeHolidaysResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeHolidaysResourceIntTest {


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

    private static final LocalDate DEFAULT_LAST_CHANGED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_CHANGED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LAST_CHANGED_BY = "AAAAA";
    private static final String UPDATED_LAST_CHANGED_BY = "BBBBB";

    @Inject
    private EmployeeHolidaysRepository employeeHolidaysRepository;

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeBillingHoursRepository employeeBillingHoursRepository;

    @Inject
    private EmployeeHolidaysService employeeHolidaysService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeHolidaysMockMvc;

    private EmployeeHolidays employeeHolidays;

    private EmployeeBillingHours employeeBillingHours;

    private Employee employee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeHolidaysResource employeeHolidaysResource = new EmployeeHolidaysResource();
        ReflectionTestUtils.setField(employeeHolidaysResource, "employeeHolidaysService", employeeHolidaysService);
        this.restEmployeeHolidaysMockMvc = MockMvcBuilders.standaloneSetup(employeeHolidaysResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
    	saveEmployeeBillingHours();
    	
        employeeHolidays = new EmployeeHolidays();
        employeeHolidays.setWeek1(DEFAULT_WEEK1);
        employeeHolidays.setWeek2(DEFAULT_WEEK2);
        employeeHolidays.setWeek3(DEFAULT_WEEK3);
        employeeHolidays.setWeek4(DEFAULT_WEEK4);
        employeeHolidays.setWeek5(DEFAULT_WEEK5);
        employeeHolidays.setLastChangedDate(DEFAULT_LAST_CHANGED_DATE);
        employeeHolidays.setLastChangedBy(DEFAULT_LAST_CHANGED_BY);
        employeeHolidays.setEmployeeBillingHours(employeeBillingHours);
    }

	private void saveEmployeeBillingHours() {
		employee = new Employee();
    	employee.setName("EmpName");
    	employee.setAssociateId(123456L);
    	employee.setDomain("Domain");

    	// Initialize the database
        employeeRepository.saveAndFlush(employee);
        
        employeeBillingHours = new EmployeeBillingHours();
        employeeBillingHours.setEmployee(employee);
        employeeBillingHours.setWeek1(DEFAULT_WEEK1);
        employeeBillingHours.setWeek2(DEFAULT_WEEK2);
        employeeBillingHours.setWeek3(DEFAULT_WEEK3);
        employeeBillingHours.setWeek4(DEFAULT_WEEK4);
        employeeBillingHours.setWeek5(DEFAULT_WEEK5);
        employeeBillingHours.setCreatedDate(LocalDate.now());
        employeeBillingHours.setForecastDate(LocalDate.now());

        // Initialize the database
        employeeBillingHoursRepository.saveAndFlush(employeeBillingHours);
	}

    @Test
    @Transactional
    public void createEmployeeHolidays() throws Exception {
        int databaseSizeBeforeCreate = employeeHolidaysRepository.findAll().size();
        
        // mock the security context for the logged in user name
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        // Create the EmployeeHolidays

        restEmployeeHolidaysMockMvc.perform(post("/api/employeeHolidayss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeHolidays)))
                .andExpect(status().isCreated());

        // Validate the EmployeeHolidays in the database
        List<EmployeeHolidays> employeeHolidayss = employeeHolidaysRepository.findAll();
        assertThat(employeeHolidayss).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeHolidays testEmployeeHolidays = employeeHolidayss.get(employeeHolidayss.size() - 1);
        assertThat(testEmployeeHolidays.getWeek1()).isEqualTo(DEFAULT_WEEK1);
        assertThat(testEmployeeHolidays.getWeek2()).isEqualTo(DEFAULT_WEEK2);
        assertThat(testEmployeeHolidays.getWeek3()).isEqualTo(DEFAULT_WEEK3);
        assertThat(testEmployeeHolidays.getWeek4()).isEqualTo(DEFAULT_WEEK4);
        assertThat(testEmployeeHolidays.getWeek5()).isEqualTo(DEFAULT_WEEK5);
        assertThat(testEmployeeHolidays.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
        assertThat(testEmployeeHolidays.getLastChangedBy()).isEqualTo("admin");
    }

    @Test
    @Transactional
    public void getAllEmployeeHolidayss() throws Exception {
        // Initialize the database
        employeeHolidaysRepository.saveAndFlush(employeeHolidays);

        // Get all the employeeHolidayss
        restEmployeeHolidaysMockMvc.perform(get("/api/employeeHolidayss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeHolidays.getId().intValue())))
                .andExpect(jsonPath("$.[*].week1").value(hasItem(DEFAULT_WEEK1)))
                .andExpect(jsonPath("$.[*].week2").value(hasItem(DEFAULT_WEEK2)))
                .andExpect(jsonPath("$.[*].week3").value(hasItem(DEFAULT_WEEK3)))
                .andExpect(jsonPath("$.[*].week4").value(hasItem(DEFAULT_WEEK4)))
                .andExpect(jsonPath("$.[*].week5").value(hasItem(DEFAULT_WEEK5)))
                .andExpect(jsonPath("$.[*].lastChangedDate").value(hasItem(DEFAULT_LAST_CHANGED_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastChangedBy").value(hasItem(DEFAULT_LAST_CHANGED_BY.toString())));
    }

    @Test
    @Transactional
    public void getEmployeeHolidays() throws Exception {
        // Initialize the database
        employeeHolidaysRepository.saveAndFlush(employeeHolidays);

        // Get the employeeHolidays
        restEmployeeHolidaysMockMvc.perform(get("/api/employeeHolidayss/{id}", employeeHolidays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employeeHolidays.getId().intValue()))
            .andExpect(jsonPath("$.week1").value(DEFAULT_WEEK1))
            .andExpect(jsonPath("$.week2").value(DEFAULT_WEEK2))
            .andExpect(jsonPath("$.week3").value(DEFAULT_WEEK3))
            .andExpect(jsonPath("$.week4").value(DEFAULT_WEEK4))
            .andExpect(jsonPath("$.week5").value(DEFAULT_WEEK5))
            .andExpect(jsonPath("$.lastChangedDate").value(DEFAULT_LAST_CHANGED_DATE.toString()))
            .andExpect(jsonPath("$.lastChangedBy").value(DEFAULT_LAST_CHANGED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeHolidays() throws Exception {
        // Get the employeeHolidays
        restEmployeeHolidaysMockMvc.perform(get("/api/employeeHolidayss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeHolidays() throws Exception {
        // Initialize the database
        employeeHolidaysRepository.saveAndFlush(employeeHolidays);

		int databaseSizeBeforeUpdate = employeeHolidaysRepository.findAll().size();
        
        // mock the security context for the logged in user name
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        // Update the employeeHolidays
        employeeHolidays.setWeek1(UPDATED_WEEK1);
        employeeHolidays.setWeek2(UPDATED_WEEK2);
        employeeHolidays.setWeek3(UPDATED_WEEK3);
        employeeHolidays.setWeek4(UPDATED_WEEK4);
        employeeHolidays.setWeek5(UPDATED_WEEK5);
        employeeHolidays.setLastChangedDate(UPDATED_LAST_CHANGED_DATE);
        employeeHolidays.setLastChangedBy(UPDATED_LAST_CHANGED_BY);

        restEmployeeHolidaysMockMvc.perform(put("/api/employeeHolidayss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeHolidays)))
                .andExpect(status().isOk());

        // Validate the EmployeeHolidays in the database
        List<EmployeeHolidays> employeeHolidayss = employeeHolidaysRepository.findAll();
        assertThat(employeeHolidayss).hasSize(databaseSizeBeforeUpdate);
        EmployeeHolidays testEmployeeHolidays = employeeHolidayss.get(employeeHolidayss.size() - 1);
        assertThat(testEmployeeHolidays.getWeek1()).isEqualTo(UPDATED_WEEK1);
        assertThat(testEmployeeHolidays.getWeek2()).isEqualTo(UPDATED_WEEK2);
        assertThat(testEmployeeHolidays.getWeek3()).isEqualTo(UPDATED_WEEK3);
        assertThat(testEmployeeHolidays.getWeek4()).isEqualTo(UPDATED_WEEK4);
        assertThat(testEmployeeHolidays.getWeek5()).isEqualTo(UPDATED_WEEK5);
        assertThat(testEmployeeHolidays.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
        assertThat(testEmployeeHolidays.getLastChangedBy()).isEqualTo("admin");
    }

    @Test
    @Transactional
    public void deleteEmployeeHolidays() throws Exception {
        // Initialize the database
        employeeHolidaysRepository.saveAndFlush(employeeHolidays);

		int databaseSizeBeforeDelete = employeeHolidaysRepository.findAll().size();

        // Get the employeeHolidays
        restEmployeeHolidaysMockMvc.perform(delete("/api/employeeHolidayss/{id}", employeeHolidays.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeHolidays> employeeHolidayss = employeeHolidaysRepository.findAll();
        assertThat(employeeHolidayss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
