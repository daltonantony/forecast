package com.opensolutions.forecast.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.opensolutions.forecast.domain.DaysOfMonth;
import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.domain.EmployeeAllocation;
import com.opensolutions.forecast.domain.EmployeeHours;
import com.opensolutions.forecast.domain.Holidays;
import com.opensolutions.forecast.repository.EmployeeHoursRepository;
import com.opensolutions.forecast.service.EmployeeAllocationService;
import com.opensolutions.forecast.service.EmployeeService;
import com.opensolutions.forecast.service.HolidaysService;

public class EmployeeHoursServiceImplTest {

	private static final String NETHERLANDS = "Netherlands";

	@Mock
	private EmployeeHoursRepository employeeHoursRepository;

	@Mock
	private EmployeeService employeeService;

    @Mock
    private EmployeeAllocationService employeeAllocationService;

    @Mock
    private HolidaysService holidaysService;

    private EmployeeHoursServiceImpl serviceImpl = new EmployeeHoursServiceImpl();

    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    	ReflectionTestUtils.setField(serviceImpl, "employeeHoursRepository", employeeHoursRepository);
    	ReflectionTestUtils.setField(serviceImpl, "employeeService", employeeService);
    	ReflectionTestUtils.setField(serviceImpl, "employeeAllocationService", employeeAllocationService);
    	ReflectionTestUtils.setField(serviceImpl, "holidaysService", holidaysService);
    }

    @Test
    public void testGetEmployeeHoursForComingMonths_ForEmpHoursNotCreatedInThisMonth() {
    	final Long empId = Long.valueOf(12345);

    	// mock the security context for the logged in user name
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(empId.toString(), empId.toString()));
        SecurityContextHolder.setContext(securityContext);

        // forecast created 4 months ago
        when(employeeHoursRepository.findAll()).thenReturn(mockEmployeeHoursList(empId, -4));

        when(employeeAllocationService.findActiveEmployeeAllocationsForEmployee(empId)).thenReturn(mockEmployeeAllocations(empId));
        when(holidaysService.getHolidaysForLocation(NETHERLANDS)).thenReturn(mockHolidaysList());

    	final Map<LocalDate, List<DaysOfMonth>> employeeHoursForComingMonths = serviceImpl.getEmployeeHoursForComingMonths();
    	assertEquals(3, employeeHoursForComingMonths.size());
    	assertEquals(LocalDate.now().plusMonths(1).withDayOfMonth(1), employeeHoursForComingMonths.keySet().toArray()[0]);
    	assertEquals(LocalDate.now().plusMonths(2).withDayOfMonth(1), employeeHoursForComingMonths.keySet().toArray()[1]);
    	assertEquals(LocalDate.now().plusMonths(3).withDayOfMonth(1), employeeHoursForComingMonths.keySet().toArray()[2]);
    }

    private List<EmployeeHours> mockEmployeeHoursList(final long empId, final int monthsToAdd) {
    	final List<EmployeeHours> employeeHoursList = new ArrayList<>();

    	final EmployeeHours employeeHours0 = new EmployeeHours();
    	employeeHours0.setEmployee(mockEmployee(empId));
    	employeeHours0.setCreatedDate(LocalDate.now().plusMonths(monthsToAdd).withDayOfMonth(1));
    	employeeHours0.setForecastDate(LocalDate.now().plusMonths(monthsToAdd + 1).withDayOfMonth(1));
    	employeeHoursList.add(employeeHours0);

    	final EmployeeHours employeeHours1 = new EmployeeHours();
    	employeeHours1.setEmployee(mockEmployee(empId));
    	employeeHours1.setCreatedDate(LocalDate.now().plusMonths(monthsToAdd).withDayOfMonth(1));
    	employeeHours1.setForecastDate(LocalDate.now().plusMonths(monthsToAdd + 2).withDayOfMonth(1));
    	employeeHoursList.add(employeeHours1);

    	final EmployeeHours employeeHours2 = new EmployeeHours();
    	employeeHours2.setEmployee(mockEmployee(empId));
    	employeeHours2.setCreatedDate(LocalDate.now().plusMonths(monthsToAdd).withDayOfMonth(1));
    	employeeHours2.setForecastDate(LocalDate.now().plusMonths(monthsToAdd + 3).withDayOfMonth(1));
    	employeeHoursList.add(employeeHours2);

    	return employeeHoursList;
    }

    private List<EmployeeAllocation> mockEmployeeAllocations(final long empId) {
    	final List<EmployeeAllocation> employeeAllocations = new ArrayList<>();

    	final EmployeeAllocation employeeAllocation0 = new EmployeeAllocation();
    	employeeAllocation0.setId(1L);
    	employeeAllocation0.setEmployee(mockEmployee(empId));
    	employeeAllocation0.setLocation(NETHERLANDS);
    	employeeAllocation0.setStartDate(LocalDate.now().minusMonths(2));
    	employeeAllocation0.setEndDate(null);
    	employeeAllocations.add(employeeAllocation0);

    	return employeeAllocations;
    }

    private Employee mockEmployee(final long empId) {
    	final Employee employee = new Employee();
    	employee.setId(1L);
    	employee.setAssociateId(empId);
    	employee.setName("Employee Name");
    	return employee;
    }

    private List<Holidays> mockHolidaysList() {
    	final List<Holidays> holidaysList = new ArrayList<>();
    	return holidaysList;
    }

}
