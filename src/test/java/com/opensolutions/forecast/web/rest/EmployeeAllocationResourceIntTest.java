package com.opensolutions.forecast.web.rest;

import com.opensolutions.forecast.Application;
import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.domain.EmployeeAllocation;
import com.opensolutions.forecast.repository.EmployeeAllocationRepository;
import com.opensolutions.forecast.repository.EmployeeRepository;
import com.opensolutions.forecast.service.EmployeeAllocationService;

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
 * Test class for the EmployeeAllocationResource REST controller.
 *
 * @see EmployeeAllocationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeAllocationResourceIntTest {

    private static final String DEFAULT_PROJECT = "AAAAA";
    private static final String UPDATED_PROJECT = "BBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    private static final Integer DEFAULT_ALLOCATION = 1;
    private static final Integer UPDATED_ALLOCATION = 2;

    private static final LocalDate DEFAULT_LAST_CHANGED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_CHANGED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_LAST_CHANGEDBY = "AAAAA";
    private static final String UPDATED_LAST_CHANGEDBY = "BBBBB";
    private static final String DEFAULT_ROLE = "AAAAA";
    private static final String UPDATED_ROLE = "BBBBB";

    @Inject
    private EmployeeAllocationRepository employeeAllocationRepository;
    
    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeAllocationService employeeAllocationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeAllocationMockMvc;

    private EmployeeAllocation employeeAllocation;
    
    private Employee employee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeAllocationResource employeeAllocationResource = new EmployeeAllocationResource();
        ReflectionTestUtils.setField(employeeAllocationResource, "employeeAllocationService", employeeAllocationService);
        this.restEmployeeAllocationMockMvc = MockMvcBuilders.standaloneSetup(employeeAllocationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
    	saveEmployee();
        
        employeeAllocation = new EmployeeAllocation();
        employeeAllocation.setProject(DEFAULT_PROJECT);
        employeeAllocation.setStartDate(DEFAULT_START_DATE);
        employeeAllocation.setEndDate(DEFAULT_END_DATE);
        employeeAllocation.setLocation(DEFAULT_LOCATION);
        employeeAllocation.setAllocation(DEFAULT_ALLOCATION);
        employeeAllocation.setLastChangedDate(DEFAULT_LAST_CHANGED_DATE);
        employeeAllocation.setLastChangedby(DEFAULT_LAST_CHANGEDBY);
        employeeAllocation.setRole(DEFAULT_ROLE);
		employeeAllocation.setEmployee(employee);
    }

	private void saveEmployee() {
		employee = new Employee();
    	employee.setName("EmpName");
    	employee.setAssociateId(123456L);
    	employee.setDomain("Domain");

    	// Initialize the database
        employeeRepository.saveAndFlush(employee);
	}

    @Test
    @Transactional
    public void createEmployeeAllocation() throws Exception {
        int databaseSizeBeforeCreate = employeeAllocationRepository.findAll().size();
        
        // mock the security context for the logged in user name
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        // Create the EmployeeAllocation

        restEmployeeAllocationMockMvc.perform(post("/api/employeeAllocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeAllocation)))
                .andExpect(status().isCreated());

        // Validate the EmployeeAllocation in the database
        List<EmployeeAllocation> employeeAllocations = employeeAllocationRepository.findAll();
        assertThat(employeeAllocations).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeAllocation testEmployeeAllocation = employeeAllocations.get(employeeAllocations.size() - 1);
        assertThat(testEmployeeAllocation.getProject()).isEqualTo(DEFAULT_PROJECT);
        assertThat(testEmployeeAllocation.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEmployeeAllocation.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEmployeeAllocation.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testEmployeeAllocation.getAllocation()).isEqualTo(DEFAULT_ALLOCATION);
        assertThat(testEmployeeAllocation.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
        assertThat(testEmployeeAllocation.getLastChangedby()).isEqualTo("admin");
        assertThat(testEmployeeAllocation.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAllocations() throws Exception {
        // Initialize the database
        employeeAllocationRepository.saveAndFlush(employeeAllocation);

        // Get all the employeeAllocations
        restEmployeeAllocationMockMvc.perform(get("/api/employeeAllocations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAllocation.getId().intValue())))
                .andExpect(jsonPath("$.[*].project").value(hasItem(DEFAULT_PROJECT.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].allocation").value(hasItem(DEFAULT_ALLOCATION)))
                .andExpect(jsonPath("$.[*].lastChangedDate").value(hasItem(DEFAULT_LAST_CHANGED_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastChangedby").value(hasItem(DEFAULT_LAST_CHANGEDBY.toString())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @Test
    @Transactional
    public void getEmployeeAllocation() throws Exception {
        // Initialize the database
        employeeAllocationRepository.saveAndFlush(employeeAllocation);

        // Get the employeeAllocation
        restEmployeeAllocationMockMvc.perform(get("/api/employeeAllocations/{id}", employeeAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employeeAllocation.getId().intValue()))
            .andExpect(jsonPath("$.project").value(DEFAULT_PROJECT.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.allocation").value(DEFAULT_ALLOCATION))
            .andExpect(jsonPath("$.lastChangedDate").value(DEFAULT_LAST_CHANGED_DATE.toString()))
            .andExpect(jsonPath("$.lastChangedby").value(DEFAULT_LAST_CHANGEDBY.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeAllocation() throws Exception {
        // Get the employeeAllocation
        restEmployeeAllocationMockMvc.perform(get("/api/employeeAllocations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeAllocation() throws Exception {
        // Initialize the database
        employeeAllocationRepository.saveAndFlush(employeeAllocation);

		int databaseSizeBeforeUpdate = employeeAllocationRepository.findAll().size();
        
        // mock the security context for the logged in user name
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        // Update the employeeAllocation
        employeeAllocation.setProject(UPDATED_PROJECT);
        employeeAllocation.setStartDate(UPDATED_START_DATE);
        employeeAllocation.setEndDate(UPDATED_END_DATE);
        employeeAllocation.setLocation(UPDATED_LOCATION);
        employeeAllocation.setAllocation(UPDATED_ALLOCATION);
        employeeAllocation.setLastChangedDate(UPDATED_LAST_CHANGED_DATE);
        employeeAllocation.setLastChangedby(UPDATED_LAST_CHANGEDBY);
        employeeAllocation.setRole(UPDATED_ROLE);

        restEmployeeAllocationMockMvc.perform(put("/api/employeeAllocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeAllocation)))
                .andExpect(status().isOk());

        // Validate the EmployeeAllocation in the database
        List<EmployeeAllocation> employeeAllocations = employeeAllocationRepository.findAll();
        assertThat(employeeAllocations).hasSize(databaseSizeBeforeUpdate);
        EmployeeAllocation testEmployeeAllocation = employeeAllocations.get(employeeAllocations.size() - 1);
        assertThat(testEmployeeAllocation.getProject()).isEqualTo(UPDATED_PROJECT);
        assertThat(testEmployeeAllocation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEmployeeAllocation.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEmployeeAllocation.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEmployeeAllocation.getAllocation()).isEqualTo(UPDATED_ALLOCATION);
        assertThat(testEmployeeAllocation.getLastChangedDate()).isEqualTo(UPDATED_LAST_CHANGED_DATE);
        assertThat(testEmployeeAllocation.getLastChangedby()).isEqualTo("admin");
        assertThat(testEmployeeAllocation.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void deleteEmployeeAllocation() throws Exception {
        // Initialize the database
        employeeAllocationRepository.saveAndFlush(employeeAllocation);

		int databaseSizeBeforeDelete = employeeAllocationRepository.findAll().size();

        // Get the employeeAllocation
        restEmployeeAllocationMockMvc.perform(delete("/api/employeeAllocations/{id}", employeeAllocation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeAllocation> employeeAllocations = employeeAllocationRepository.findAll();
        assertThat(employeeAllocations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
