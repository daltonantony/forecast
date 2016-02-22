package com.opensolutions.forecast.service.impl;

import com.opensolutions.forecast.domain.DaysOfMonth;
import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.domain.EmployeeAllocation;
import com.opensolutions.forecast.domain.EmployeeHours;
import com.opensolutions.forecast.domain.Holidays;
import com.opensolutions.forecast.repository.EmployeeHoursRepository;
import com.opensolutions.forecast.repository.search.EmployeeHoursSearchRepository;
import com.opensolutions.forecast.security.SecurityUtils;
import com.opensolutions.forecast.service.EmployeeAllocationService;
import com.opensolutions.forecast.service.EmployeeHoursService;
import com.opensolutions.forecast.service.EmployeeService;
import com.opensolutions.forecast.service.HolidaysService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing EmployeeHours.
 */
@Service
@Transactional
public class EmployeeHoursServiceImpl implements EmployeeHoursService {

    private static final EnumSet<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private final Logger log = LoggerFactory.getLogger(EmployeeHoursServiceImpl.class);

    @Inject
    private EmployeeHoursRepository employeeHoursRepository;

    @Inject
    private EmployeeHoursSearchRepository employeeHoursSearchRepository;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private EmployeeAllocationService employeeAllocationService;
    
    @Inject
    private HolidaysService holidaysService;

    /**
     * Save a employeeHours.
     * @return the persisted entity
     */
    public EmployeeHours save(EmployeeHours employeeHours) {
        log.debug("Request to save EmployeeHours : {}", employeeHours);
        EmployeeHours result = employeeHoursRepository.save(employeeHours);
        employeeHoursSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the employeeHourss.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EmployeeHours> findAll() {
        log.debug("Request to get all EmployeeHourss");
        List<EmployeeHours> result = employeeHoursRepository.findAll();
        return result;
    }

    /**
     *  get one employeeHours by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EmployeeHours findOne(Long id) {
        log.debug("Request to get EmployeeHours : {}", id);
        EmployeeHours employeeHours = employeeHoursRepository.findOne(id);
        return employeeHours;
    }

    /**
     *  delete the  employeeHours by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeeHours : {}", id);
        employeeHoursRepository.delete(id);
        employeeHoursSearchRepository.delete(id);
    }

    /**
     * search for the employeeHours corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<EmployeeHours> search(String query) {
        log.debug("REST request to search EmployeeHourss for query {}", query);
        return StreamSupport
            .stream(employeeHoursSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<DaysOfMonth>> getEmployeeHoursForComingMonths() {
    	final String employeeLocation = getEmployeeLocation();
    	final List<Holidays> holidays = holidaysService.getHolidaysForLocation(employeeLocation);
        final Map<String, List<DaysOfMonth>> daysOfMonthMap = new LinkedHashMap<>();
        daysOfMonthMap.put(LocalDate.now().plusMonths(1).getMonth().toString(), getDaysOfMonths(LocalDate.now().plusMonths(1), holidays));
        daysOfMonthMap.put(LocalDate.now().plusMonths(2).getMonth().toString(), getDaysOfMonths(LocalDate.now().plusMonths(2), holidays));
        daysOfMonthMap.put(LocalDate.now().plusMonths(3).getMonth().toString(), getDaysOfMonths(LocalDate.now().plusMonths(3), holidays));
        return daysOfMonthMap;
    }

    private List<DaysOfMonth> getDaysOfMonths(final LocalDate localDate, final List<Holidays> holidays) {
        final List<DaysOfMonth> daysOfMonths = new ArrayList<>();
        for (int i = 1; i <= localDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final DaysOfMonth daysOfMonth = new DaysOfMonth();
            daysOfMonth.setDay(i);
            daysOfMonth.setHoliday(WEEKEND.contains(localDate.withDayOfMonth(i).getDayOfWeek()) || isHoliday(holidays, localDate.withDayOfMonth(i)));
            daysOfMonths.add(daysOfMonth);
        }
        return daysOfMonths;
    }

    private String getEmployeeLocation() {
    	final Long empId = Long.valueOf(SecurityUtils.getCurrentUserLogin());
    	final List<EmployeeAllocation> activeEmployeeAllocations = employeeAllocationService.findActiveEmployeeAllocationsForEmployee(empId);
    	// Assuming that the location is the same even if employee has multiple active allocations:
    	final EmployeeAllocation employeeAllocation = activeEmployeeAllocations.size() > 0 ? activeEmployeeAllocations.get(0) : null;
    	if (employeeAllocation == null) {
			// TODO: handle lack of active allocation for the logged in employee
    		throw new RuntimeException("No active allocation for the employee: " + empId);
		}
    	log.debug("Retrieved allocation for employee: {}", employeeAllocation.getEmployee().getName());
		return employeeAllocation.getLocation();
	}

	private boolean isHoliday(final List<Holidays> holidays, final LocalDate localDate) {
        for (final Holidays holiday : holidays) {
            final boolean isDateInHolidayRange = !(localDate.isBefore(holiday.getStartDate()) || localDate.isAfter(holiday.getEndDate()));
			if (isDateInHolidayRange) {
                return true;
            }
        }
        return false;
    }

	@Override
	public Employee saveEmployeeHoursForComingMonths(Map<String, List<DaysOfMonth>> employeeHoursForComingMonths) {
		log.debug("Request to save the Employee Hours for Coming Months");
		final Long empId = Long.valueOf(SecurityUtils.getCurrentUserLogin());
		final Employee employee = employeeService.getEmployeeForAssociateId(empId);
		
		// TODO: convert to EmployeeHours ad save to DB
		for (Entry<String, List<DaysOfMonth>> entry : employeeHoursForComingMonths.entrySet()) {
			log.debug("Month: {}", entry.getKey());
			for (DaysOfMonth dayOfMonth : entry.getValue()) {
				log.debug("Day: {} - {} - {}", dayOfMonth.getDay(), dayOfMonth.isSelected(), dayOfMonth.isHoliday());
			}
			log.debug("---------------------------");
		}
		log.debug("Employee Hours saved for [{}]", employee.getAssociateId());
		return employee;
	}
}
