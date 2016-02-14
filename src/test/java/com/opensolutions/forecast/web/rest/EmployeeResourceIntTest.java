package com.opensolutions.forecast.web.rest;

import com.opensolutions.forecast.Application;
import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.repository.EmployeeRepository;
import com.opensolutions.forecast.service.EmployeeService;

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
import org.springframework.security.core.userdetails.User;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EmployeeResource REST controller.
 *
 * @see EmployeeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Long DEFAULT_ASSOCIATE_ID = 1L;
    private static final Long UPDATED_ASSOCIATE_ID = 2L;
    private static final String DEFAULT_CLIENT_ID = "AAAAA";
    private static final String UPDATED_CLIENT_ID = "BBBBB";
    private static final String DEFAULT_DOMAIN = "AAAAA";
    private static final String UPDATED_DOMAIN = "BBBBB";

    private static final LocalDate DEFAULT_LAST_CHANGED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_CHANGED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LAST_CHANGED_BY = "AAAAA";
    private static final String UPDATED_LAST_CHANGED_BY = "BBBBB";

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeResource employeeResource = new EmployeeResource();
        ReflectionTestUtils.setField(employeeResource, "employeeService", employeeService);
        this.restEmployeeMockMvc = MockMvcBuilders.standaloneSetup(employeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employee = new Employee();
        employee.setName(DEFAULT_NAME);
        employee.setAssociateId(DEFAULT_ASSOCIATE_ID);
        employee.setClientId(DEFAULT_CLIENT_ID);
        employee.setDomain(DEFAULT_DOMAIN);
        employee.setLastChangedDate(DEFAULT_LAST_CHANGED_DATE);
        employee.setLastChangedBy(DEFAULT_LAST_CHANGED_BY);
    }

    @Test
    @Transactional
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // mock the security context for the logged in user name
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        User user = new User("admin", "admin", Collections.emptyList());
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user,""));
        SecurityContextHolder.setContext(securityContext);

        // Create the Employee
        restEmployeeMockMvc.perform(post("/api/employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employee)))
                .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employees.get(employees.size() - 1);
        assertThat(testEmployee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployee.getAssociateId()).isEqualTo(DEFAULT_ASSOCIATE_ID);
        assertThat(testEmployee.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
        assertThat(testEmployee.getDomain()).isEqualTo(DEFAULT_DOMAIN);
        assertThat(testEmployee.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
        assertThat(testEmployee.getLastChangedBy()).isEqualTo("admin");
    }

    @Test
    @Transactional
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employees
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].associateId").value(hasItem(DEFAULT_ASSOCIATE_ID.intValue())))
                .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
                .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
                .andExpect(jsonPath("$.[*].lastChangedDate").value(hasItem(DEFAULT_LAST_CHANGED_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastChangedBy").value(hasItem(DEFAULT_LAST_CHANGED_BY.toString())));
    }

    @Test
    @Transactional
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.associateId").value(DEFAULT_ASSOCIATE_ID.intValue()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.toString()))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()))
            .andExpect(jsonPath("$.lastChangedDate").value(DEFAULT_LAST_CHANGED_DATE.toString()))
            .andExpect(jsonPath("$.lastChangedBy").value(DEFAULT_LAST_CHANGED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

		int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // mock the security context for the logged in user name
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        User user = new User("admin", "admin", Collections.emptyList());
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user,""));
        SecurityContextHolder.setContext(securityContext);

        // Update the employee
        employee.setName(UPDATED_NAME);
        employee.setAssociateId(UPDATED_ASSOCIATE_ID);
        employee.setClientId(UPDATED_CLIENT_ID);
        employee.setDomain(UPDATED_DOMAIN);
        employee.setLastChangedDate(UPDATED_LAST_CHANGED_DATE);
        employee.setLastChangedBy(UPDATED_LAST_CHANGED_BY);

        restEmployeeMockMvc.perform(put("/api/employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employee)))
                .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employees.get(employees.size() - 1);
        assertThat(testEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployee.getAssociateId()).isEqualTo(UPDATED_ASSOCIATE_ID);
        assertThat(testEmployee.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
        assertThat(testEmployee.getDomain()).isEqualTo(UPDATED_DOMAIN);
        assertThat(testEmployee.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
        assertThat(testEmployee.getLastChangedBy()).isEqualTo("admin");
    }

    @Test
    @Transactional
    public void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

		int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Get the employee
        restEmployeeMockMvc.perform(delete("/api/employees/{id}", employee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeDelete - 1);
    }
}
