package com.opensolutions.forecast.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Override
	public EmployeeHours save(final EmployeeHours employeeHours) {
        log.debug("Request to save EmployeeHours : {}", employeeHours);
        final EmployeeHours result = employeeHoursRepository.save(employeeHours);
        employeeHoursSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the employeeHourss.
     *  @return the list of entities
     */
    @Override
	@Transactional(readOnly = true)
    public List<EmployeeHours> findAll() {
        log.debug("Request to get all EmployeeHourss");
        final List<EmployeeHours> result = employeeHoursRepository.findAll();
        return result;
    }

    /**
     *  get one employeeHours by id.
     *  @return the entity
     */
    @Override
	@Transactional(readOnly = true)
    public EmployeeHours findOne(final Long id) {
        log.debug("Request to get EmployeeHours : {}", id);
        final EmployeeHours employeeHours = employeeHoursRepository.findOne(id);
        return employeeHours;
    }

    /**
     *  delete the  employeeHours by id.
     */
    @Override
	public void delete(final Long id) {
        log.debug("Request to delete EmployeeHours : {}", id);
        employeeHoursRepository.delete(id);
        employeeHoursSearchRepository.delete(id);
    }

    /**
     * search for the employeeHours corresponding
     * to the query.
     */
    @Override
	@Transactional(readOnly = true)
    public List<EmployeeHours> search(final String query) {
        log.debug("REST request to search EmployeeHourss for query {}", query);
        return StreamSupport
            .stream(employeeHoursSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<DaysOfMonth>> getEmployeeHoursForComingMonths() {
    	final Long empId = Long.valueOf(SecurityUtils.getCurrentUserLogin());
    	log.debug("Employee Id: {}", empId.toString());
    	final List<EmployeeHours> employeeHoursCreatedInCurrentMonth = getEmployeeHoursCreatedInCurrentMonth(empId);

    	final String employeeLocation = getEmployeeLocation(empId);
    	final List<Holidays> holidays = holidaysService.getHolidaysForLocation(employeeLocation);
        final Map<String, List<DaysOfMonth>> daysOfMonthMap = getDaysOfMonthMap(employeeHoursCreatedInCurrentMonth, holidays);

        return daysOfMonthMap;
    }

	private List<EmployeeHours> getEmployeeHoursCreatedInCurrentMonth(final Long empId) {
		final List<EmployeeHours> allEmployeeHoursForSelectedEmployee = new ArrayList<>();
    	for (final EmployeeHours employeeHours : findAll()) {
    		if (employeeHours.getEmployee().getAssociateId().equals(empId)) {
				allEmployeeHoursForSelectedEmployee.add(employeeHours);
			}
    	}

    	final List<EmployeeHours> employeeHoursCreatedInCurrentMonth = new ArrayList<>();
    	for (final EmployeeHours employeeHours : allEmployeeHoursForSelectedEmployee) {
			if (employeeHours.getCreatedDate().getMonth() == LocalDate.now().getMonth()) {
				employeeHoursCreatedInCurrentMonth.add(employeeHours);
			}
		}
		return employeeHoursCreatedInCurrentMonth;
	}

	private Map<String, List<DaysOfMonth>> getDaysOfMonthMap(final List<EmployeeHours> employeeHoursCreatedInCurrentMonth, final List<Holidays> holidays) {
		final Map<String, List<DaysOfMonth>> daysOfMonthMap = new LinkedHashMap<>();

        if (CollectionUtils.isNotEmpty(employeeHoursCreatedInCurrentMonth)) {
        	log.debug("Employee Hours created this month are already present");
        	for (final EmployeeHours employeeHours : employeeHoursCreatedInCurrentMonth) {
        		final String employeeHolidays = employeeHours.getHolidays();
        		final List<String> empHolidays = Arrays.asList(ArrayUtils.nullToEmpty(StringUtils.split(employeeHolidays, ',')));
        		final LocalDate forecastDate = employeeHours.getForecastDate();
				daysOfMonthMap.put(forecastDate.getMonth().toString(), getDaysOfMonths(forecastDate, holidays, true, empHolidays));
        	}
		} else {
			log.debug("Employee Hours created this month are not already present");
			daysOfMonthMap.put(LocalDate.now().plusMonths(1).getMonth().toString(), getDaysOfMonths(LocalDate.now().plusMonths(1), holidays, false, null));
			daysOfMonthMap.put(LocalDate.now().plusMonths(2).getMonth().toString(), getDaysOfMonths(LocalDate.now().plusMonths(2), holidays, false, null));
			daysOfMonthMap.put(LocalDate.now().plusMonths(3).getMonth().toString(), getDaysOfMonths(LocalDate.now().plusMonths(3), holidays, false, null));
		}

		return daysOfMonthMap;
	}

    private List<DaysOfMonth> getDaysOfMonths(final LocalDate localDate, final List<Holidays> holidays, final boolean hoursAlreadyAvailable, final List<String> empHolidays) {
        final List<DaysOfMonth> daysOfMonths = new ArrayList<>();
        log.debug("Setting days of month for {}", localDate.getMonth().toString());
        for (int i = 1; i <= localDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final DaysOfMonth daysOfMonth = new DaysOfMonth();
            daysOfMonth.setDay(i);
            daysOfMonth.setHoliday(WEEKEND.contains(localDate.withDayOfMonth(i).getDayOfWeek()) || isHoliday(holidays, localDate.withDayOfMonth(i)));
            if (hoursAlreadyAvailable) {
				daysOfMonth.setSelected(empHolidays.contains(String.valueOf(i)));
			}
            daysOfMonths.add(daysOfMonth);
        }
        return daysOfMonths;
    }

    private String getEmployeeLocation(final Long empId) {
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
	public Employee saveEmployeeHoursForComingMonths(final Map<String, List<DaysOfMonth>> employeeHoursForComingMonths) {
		// TODO: check for existing entries and update that instead of creating new entries
		log.debug("Request to save the Employee Hours for Coming Months");
		final Long empId = Long.valueOf(SecurityUtils.getCurrentUserLogin());
		final Employee employee = employeeService.getEmployeeForAssociateId(empId);

		int index = 1;
		for (final Entry<String, List<DaysOfMonth>> entry : employeeHoursForComingMonths.entrySet()) {
			final String month = entry.getKey();
			log.debug("Month: {}", month);

			final EmployeeHours employeeHours = new EmployeeHours();
			employeeHours.setEmployee(employee);
			employeeHours.setCreatedDate(LocalDate.now());

			// TODO: link with month value instead of index
			employeeHours.setForecastDate(LocalDate.now().plusMonths(index).with(TemporalAdjusters.firstDayOfMonth()));

			final StringBuffer employeeHolidays = new StringBuffer();
			for (final DaysOfMonth dayOfMonth : entry.getValue()) {
				log.debug("Day: {} - {} - {}", dayOfMonth.getDay(), dayOfMonth.isSelected(), dayOfMonth.isHoliday());
				if (!dayOfMonth.isHoliday() && dayOfMonth.isSelected()) {
					employeeHolidays.append(dayOfMonth.getDay());
					employeeHolidays.append(",");
				}
			}
			log.debug("---------------------------");

			// Remove the last comma, if applicable:
			employeeHolidays.setLength(Math.max(employeeHolidays.length() - 1, 0));
			employeeHours.setHolidays(employeeHolidays.toString());

			employeeHours.setLastChangedBy(empId.toString());
			employeeHours.setLastChangedDate(LocalDate.now());

			save(employeeHours);
			index++;
		}
		log.debug("Employee Hours saved for [{}]", employee.getAssociateId());
		return employee;
	}
}
