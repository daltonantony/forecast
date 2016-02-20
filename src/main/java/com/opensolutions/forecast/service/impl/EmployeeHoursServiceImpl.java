package com.opensolutions.forecast.service.impl;

import com.opensolutions.forecast.domain.DaysOfMonth;
import com.opensolutions.forecast.domain.EmployeeHours;
import com.opensolutions.forecast.domain.Holidays;
import com.opensolutions.forecast.repository.EmployeeHoursRepository;
import com.opensolutions.forecast.repository.HolidaysRepository;
import com.opensolutions.forecast.repository.search.EmployeeHoursSearchRepository;
import com.opensolutions.forecast.service.EmployeeHoursService;
import com.opensolutions.forecast.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing EmployeeHours.
 */
@Service
@Transactional
public class EmployeeHoursServiceImpl implements EmployeeHoursService {

    private static final EnumSet<DayOfWeek> HOLIDAY_OF_WEEKS = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private final Logger log = LoggerFactory.getLogger(EmployeeHoursServiceImpl.class);

    @Inject
    private EmployeeHoursRepository employeeHoursRepository;

    @Inject
    private EmployeeHoursSearchRepository employeeHoursSearchRepository;

    @Inject
    private HolidaysRepository holidaysRepository;

    @Inject
    private EmployeeService employeeService;

    /**
     * Save a employeeHours.
     *
     * @return the persisted entity
     */
    public EmployeeHours save(EmployeeHours employeeHours) {
        log.debug("Request to save EmployeeHours : {}", employeeHours);
        EmployeeHours result = employeeHoursRepository.save(employeeHours);
        employeeHoursSearchRepository.save(result);
        return result;
    }

    /**
     * get all the employeeHourss.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EmployeeHours> findAll() {
        log.debug("Request to get all EmployeeHourss");
        List<EmployeeHours> result = employeeHoursRepository.findAll();
        return result;
    }

    /**
     * get one employeeHours by id.
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public EmployeeHours findOne(Long id) {
        log.debug("Request to get EmployeeHours : {}", id);
        EmployeeHours employeeHours = employeeHoursRepository.findOne(id);
        return employeeHours;
    }

    /**
     * delete the  employeeHours by id.
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
        final Map<String, List<DaysOfMonth>> daysOfMonthMap = new LinkedHashMap<>();
        daysOfMonthMap.put(LocalDate.now().plusMonths(1).getMonth().toString(), getDaysOfMonths(LocalDate.now().plusMonths(1)));
        daysOfMonthMap.put(LocalDate.now().plusMonths(2).getMonth().toString(), getDaysOfMonths(LocalDate.now().plusMonths(2)));
        daysOfMonthMap.put(LocalDate.now().plusMonths(3).getMonth().toString(), getDaysOfMonths(LocalDate.now().plusMonths(3)));
        return daysOfMonthMap;
    }

    private List<DaysOfMonth> getDaysOfMonths(final LocalDate localDate) {
        final List<DaysOfMonth> daysOfMonths = new ArrayList<>();
        for (int i = 1; i <= localDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final DaysOfMonth daysOfMonth = new DaysOfMonth();
            daysOfMonth.setDay(i);
            daysOfMonth.setHoliday(HOLIDAY_OF_WEEKS.contains(localDate.withDayOfMonth(i).getDayOfWeek()) || isHoliday("Netherlands", localDate.withDayOfMonth(i)));
            daysOfMonths.add(daysOfMonth);
        }
        return daysOfMonths;
    }

    private boolean isHoliday(final String location, final LocalDate localDate) {
        final List<Holidays> holidaysList = holidaysRepository.findAll();
        for (Holidays holiday : holidaysList) {
            if (holiday.getLocation().equalsIgnoreCase(location) && holiday.getStartDate().equals(localDate)) {
                return true;
            }
        }
        return false;
    }
}
