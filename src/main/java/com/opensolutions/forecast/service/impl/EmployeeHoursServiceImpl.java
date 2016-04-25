package com.opensolutions.forecast.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensolutions.forecast.domain.CodeValues;
import com.opensolutions.forecast.domain.DaysOfMonth;
import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.domain.EmployeeAllocation;
import com.opensolutions.forecast.domain.EmployeeForecast;
import com.opensolutions.forecast.domain.EmployeeHours;
import com.opensolutions.forecast.domain.Holidays;
import com.opensolutions.forecast.repository.EmployeeHoursRepository;
import com.opensolutions.forecast.repository.search.EmployeeHoursSearchRepository;
import com.opensolutions.forecast.security.SecurityUtils;
import com.opensolutions.forecast.service.CodeValuesService;
import com.opensolutions.forecast.service.EmployeeAllocationService;
import com.opensolutions.forecast.service.EmployeeHoursService;
import com.opensolutions.forecast.service.EmployeeService;
import com.opensolutions.forecast.service.HolidaysService;
import com.opensolutions.forecast.service.util.ForecastDownloadHelper;

/**
 * Service Implementation for managing EmployeeHours.
 */
@Service
@Transactional
public class EmployeeHoursServiceImpl implements EmployeeHoursService {

    private final Logger log = LoggerFactory.getLogger(EmployeeHoursServiceImpl.class);
    private static final String FORECAST_FREEZE_DATE = "ForecastFreezeDate";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final EnumSet<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

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

    @Inject
    private CodeValuesService codeValuesService;

    private final ForecastDownloadHelper downloadHelper = new ForecastDownloadHelper();

    /**
     * Save a employeeHours.
     *
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
     * get all the employeeHourss.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeHours> findAll() {
        log.debug("Request to get all EmployeeHours");
        return employeeHoursRepository.findAll();
    }

    /**
     * get one employeeHours by id.
     *
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeHours findOne(final Long id) {
        log.debug("Request to get EmployeeHours : {}", id);
        return employeeHoursRepository.findOne(id);
    }

    /**
     * delete the employeeHours by id.
     */
    @Override
    public void delete(final Long id) {
        log.debug("Request to delete EmployeeHours : {}", id);
        employeeHoursRepository.delete(id);
        employeeHoursSearchRepository.delete(id);
    }

    /**
     * search for the employeeHours corresponding to the query.
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
    @Transactional(readOnly = true)
    public Map<LocalDate, List<DaysOfMonth>> getEmployeeHoursForComingMonths() {
        final Long empId = Long.valueOf(SecurityUtils.getCurrentUserLogin());
        return getForecastedHours(empId);
    }

    private Map<LocalDate, List<DaysOfMonth>> getForecastedHours(final Long empId) {
        log.debug("Employee Hours for [{}]", empId.toString());

        final List<EmployeeHours> employeeHoursCreatedInCurrentMonth = getEmployeeHoursCreatedInCurrentMonth(empId);
        final String employeeLocation = getEmployeeLocation(empId);
        final List<Holidays> holidays = holidaysService.getHolidaysForLocation(employeeLocation);

        return getDaysOfMonthMap(employeeHoursCreatedInCurrentMonth, holidays);
    }

    private List<EmployeeHours> getEmployeeHoursCreatedInCurrentMonth(final Long empId) {
        final List<EmployeeHours> allEmployeeHoursForSelectedEmployee = getAllEmployeeHoursForEmployee(empId);

        final List<EmployeeHours> employeeHoursCreatedInCurrentMonth = new ArrayList<>();
        for (final EmployeeHours employeeHours : allEmployeeHoursForSelectedEmployee) {
            if (employeeHours.getCreatedDate().withDayOfMonth(1).equals(LocalDate.now().withDayOfMonth(1))) {
                employeeHoursCreatedInCurrentMonth.add(employeeHours);
            }
        }
        return employeeHoursCreatedInCurrentMonth;
    }

    private List<EmployeeHours> getAllEmployeeHoursForEmployee(final Long empId) {
        final List<EmployeeHours> allEmployeeHoursForSelectedEmployee = new ArrayList<>();
        // TODO: try to use named query using empId instead of calling findAll()
        for (final EmployeeHours employeeHours : findAll()) {
            if (employeeHours.getEmployee().getAssociateId().equals(empId)) {
                allEmployeeHoursForSelectedEmployee.add(employeeHours);
            }
        }
        return allEmployeeHoursForSelectedEmployee;
    }

    private Map<LocalDate, List<DaysOfMonth>> getDaysOfMonthMap(final List<EmployeeHours> employeeHoursCreatedInCurrentMonth,
                                                                final List<Holidays> holidays)
    {
        final Map<LocalDate, List<DaysOfMonth>> daysOfMonthMap = new LinkedHashMap<>();

        if (CollectionUtils.isNotEmpty(employeeHoursCreatedInCurrentMonth)) {
            log.debug("Employee Hours created this month are already present");
            for (final EmployeeHours employeeHours : employeeHoursCreatedInCurrentMonth) {
                final String employeeHolidays = employeeHours.getHolidays();
                final List<String> empHolidays =
                        Arrays.asList(ArrayUtils.nullToEmpty(StringUtils.split(employeeHolidays, ',')));
                final LocalDate forecastDate = employeeHours.getForecastDate();
                daysOfMonthMap.put(forecastDate, getDaysOfMonths(forecastDate, holidays, true, empHolidays));
            }
        } else {
            log.debug("Employee Hours created this month are not already present");
            daysOfMonthMap.put(LocalDate.now().plusMonths(1).withDayOfMonth(1),
                getDaysOfMonthsForEmpHoursNotCreatedInThisMonth(holidays, 1));
            daysOfMonthMap.put(LocalDate.now().plusMonths(2).withDayOfMonth(1),
                getDaysOfMonthsForEmpHoursNotCreatedInThisMonth(holidays, 2));
            daysOfMonthMap.put(LocalDate.now().plusMonths(3).withDayOfMonth(1),
                getDaysOfMonthsForEmpHoursNotCreatedInThisMonth(holidays, 3));
        }

        return daysOfMonthMap;
    }

    private List<DaysOfMonth> getDaysOfMonthsForEmpHoursNotCreatedInThisMonth(final List<Holidays> holidays,
                                                                              final long monthsToAdd)
    {
        return getDaysOfMonths(LocalDate.now().plusMonths(monthsToAdd), holidays, false, null);
    }

    private List<DaysOfMonth> getDaysOfMonths(final LocalDate localDate, final List<Holidays> holidays,
                                              final boolean hoursAlreadyAvailable, final List<String> empHolidays)
    {
        final List<DaysOfMonth> daysOfMonths = new ArrayList<>();
        log.debug("Setting days of month for {}", localDate.getMonth().toString());
        for (int i = 1; i <= localDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final DaysOfMonth daysOfMonth = new DaysOfMonth();
            daysOfMonth.setDay(i);
            daysOfMonth.setHoliday(WEEKEND.contains(localDate.withDayOfMonth(i).getDayOfWeek())
                    || isHoliday(holidays, localDate.withDayOfMonth(i)));
            if (hoursAlreadyAvailable) {
                daysOfMonth.setSelected(empHolidays.contains(String.valueOf(i)));
            }
            daysOfMonths.add(daysOfMonth);
        }
        return daysOfMonths;
    }

    private String getEmployeeLocation(final Long empId) {
        final List<EmployeeAllocation> activeEmployeeAllocations =
                employeeAllocationService.findActiveEmployeeAllocationsForEmployee(empId);
        // Assuming that the location is the same even if employee has multiple active allocations:
        final EmployeeAllocation employeeAllocation =
                activeEmployeeAllocations.size() > 0 ? activeEmployeeAllocations.get(0) : null;
        if (employeeAllocation == null) {
            // TODO: handle lack of active allocation for the logged in employee
            throw new RuntimeException("No active allocation for the employee: " + empId);
        }
        log.debug("Retrieved allocation for employee: {}", employeeAllocation.getEmployee().getName());
        return employeeAllocation.getLocation();
    }

    private boolean isHoliday(final List<Holidays> holidays, final LocalDate localDate) {
        for (final Holidays holiday : holidays) {
            final boolean isDateInHolidayRange =
                    !(localDate.isBefore(holiday.getStartDate()) || localDate.isAfter(holiday.getEndDate()));
            if (isDateInHolidayRange) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Employee saveEmployeeHoursForComingMonths(final Map<LocalDate, List<DaysOfMonth>> employeeHoursForComingMonths) {
        log.debug("Request to save the Employee Hours for Coming Months");
        final Long empId = Long.valueOf(SecurityUtils.getCurrentUserLogin());
        final Employee employee = employeeService.getEmployeeForAssociateId(empId);
        final List<EmployeeHours> employeeHoursCreatedInCurrentMonth = getEmployeeHoursCreatedInCurrentMonth(empId);

        for (final Entry<LocalDate, List<DaysOfMonth>> entry : employeeHoursForComingMonths.entrySet()) {
            final LocalDate forecastDate = entry.getKey();
            log.debug("Forecast Month: {}", forecastDate.getMonth().toString());
            if (CollectionUtils.isNotEmpty(employeeHoursCreatedInCurrentMonth)) {
                updateEmployeeHoursAlreadyPresentForThisMonth(empId, employeeHoursCreatedInCurrentMonth, entry,
                    forecastDate);
            } else {
                createEmployeeHoursNewlyCreatedForThisMonth(employee, entry, forecastDate);
            }
        }
        log.debug("Employee Hours saved for [{}]", employee.getAssociateId());
        return employee;
    }

    private void updateEmployeeHoursAlreadyPresentForThisMonth(final Long empId,
                                                               final List<EmployeeHours> employeeHoursCreatedInCurrentMonth,
                                                               final Entry<LocalDate, List<DaysOfMonth>> entry,
                                                               final LocalDate forecastDate)
    {
        log.debug("Employee Hours created this month are already present");
        for (final EmployeeHours employeeHours : employeeHoursCreatedInCurrentMonth) {
            if (employeeHours.getForecastDate().equals(forecastDate)) {
                setEmployeeHoursCommonFields(entry, employeeHours, empId);
                save(employeeHours);
            }
        }
    }

    private void createEmployeeHoursNewlyCreatedForThisMonth(final Employee employee,
                                                             final Entry<LocalDate, List<DaysOfMonth>> entry,
                                                             final LocalDate forecastDate)
    {
        log.debug("Employee Hours created this month are not already present");
        final EmployeeHours employeeHours = new EmployeeHours();
        employeeHours.setEmployee(employee);
        employeeHours.setForecastDate(forecastDate);
        setEmployeeHoursCommonFields(entry, employeeHours, employee.getAssociateId());
        save(employeeHours);
    }

    private void setEmployeeHoursCommonFields(final Entry<LocalDate, List<DaysOfMonth>> entry,
                                              final EmployeeHours employeeHours, final Long empId)
    {
        employeeHours.setCreatedDate(LocalDate.now().withDayOfMonth(1));
        setEmployeeHolidays(entry, employeeHours);
        employeeHours.setLastChangedDate(LocalDate.now());
        employeeHours.setLastChangedBy(empId.toString());
    }

    private void setEmployeeHolidays(final Entry<LocalDate, List<DaysOfMonth>> entry,
                                     final EmployeeHours employeeHours)
    {
        // Joining employee holidays, separated by a comma
        final StringBuilder employeeHolidays = new StringBuilder();
        for (final DaysOfMonth dayOfMonth : entry.getValue()) {
            log.debug("Day: {} - Sel: {} - Hol: {}", dayOfMonth.getDay(), dayOfMonth.isSelected(),
                dayOfMonth.isHoliday());
            if (!dayOfMonth.isHoliday() && dayOfMonth.isSelected()) {
                employeeHolidays.append(dayOfMonth.getDay());
                employeeHolidays.append(",");
            }
        }
        log.debug("---------------------------");

        // Remove the last comma, if applicable:
        employeeHolidays.setLength(Math.max(employeeHolidays.length() - 1, 0));
        employeeHours.setHolidays(employeeHolidays.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<LocalDate, List<EmployeeHours>> getEmployeeHoursForPreviousMonths() {
        final Long empId = Long.valueOf(SecurityUtils.getCurrentUserLogin());
        log.debug("Employee Hours for [{}]", empId.toString());

        final List<EmployeeHours> allEmployeeHoursForSelectedEmployee = getAllEmployeeHoursForEmployee(empId);
        sortEmployeeHoursOnDescendingOrderOfCreatedDate(allEmployeeHoursForSelectedEmployee);

        final Map<LocalDate, List<EmployeeHours>> employeeHoursMapForPreviousMonths = new LinkedHashMap<>();
        for (final EmployeeHours employeeHours : allEmployeeHoursForSelectedEmployee) {
            addPreviousMonthsEmployeeHoursToMap(employeeHoursMapForPreviousMonths, employeeHours);
        }

        return employeeHoursMapForPreviousMonths;
    }

    private void sortEmployeeHoursOnDescendingOrderOfCreatedDate(final List<EmployeeHours> allEmployeeHoursForSelectedEmployee) {
        Collections.sort(allEmployeeHoursForSelectedEmployee, new Comparator<EmployeeHours>() {

            @Override
            public int compare(final EmployeeHours o1, final EmployeeHours o2) {
                if (o1.getCreatedDate().equals(o2.getCreatedDate())) {
                    return 0;
                }
                return o1.getCreatedDate().isAfter(o2.getCreatedDate()) ? -1 : 1;
            }
        });
    }

    private void addPreviousMonthsEmployeeHoursToMap(final Map<LocalDate, List<EmployeeHours>> employeeHoursMapForPreviousMonths,
                                                     final EmployeeHours employeeHours)
    {
        final LocalDate createdDate = employeeHours.getCreatedDate();
        if (createdDate.withDayOfMonth(1).isBefore(LocalDate.now().withDayOfMonth(1))) {
            if (employeeHoursMapForPreviousMonths.containsKey(createdDate)) {
                employeeHoursMapForPreviousMonths.get(createdDate).add(employeeHours);
            } else {
                final List<EmployeeHours> employeeHoursList = new ArrayList<>();
                employeeHoursList.add(employeeHours);
                employeeHoursMapForPreviousMonths.put(createdDate, employeeHoursList);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isForecastFreezePeriod() {
        final LocalDate forecastFreezeDate = getForecastFreezeDateFromDB();
        final LocalDate today = LocalDate.now();
        return !today.isBefore(forecastFreezeDate)
                && !today.isAfter(forecastFreezeDate.with(TemporalAdjusters.lastDayOfMonth()));
    }

    @Override
    @Transactional(readOnly = false)
    public LocalDate getForecastFreezeDate() {
        LocalDate forecastFreezeDate = getForecastFreezeDateFromDB();
        final LocalDate today = LocalDate.now();
        if (forecastFreezeDate.getYear() != today.getYear() || forecastFreezeDate.getMonth() != today.getMonth()) {
            // if a freeze date is not already present for the current month, set a default date in database
            forecastFreezeDate = today.with(TemporalAdjusters.lastDayOfMonth()).minusDays(5);
            setForecastFreezeDate(forecastFreezeDate);
        }
        return forecastFreezeDate;
    }

    private LocalDate getForecastFreezeDateFromDB() {
        final List<CodeValues> codeValues = codeValuesService.searchActiveCodeValues(FORECAST_FREEZE_DATE);
        final String codeValue = codeValues.get(0).getCodeValue();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        return LocalDate.parse(codeValue, formatter);
    }

    @Override
    @Transactional(readOnly = false)
    public void setForecastFreezeDate(final LocalDate forecastFreezeDate) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        final String forecastFreezeDateString = formatter.format(forecastFreezeDate);

        final List<CodeValues> codeValues = codeValuesService.searchActiveCodeValues(FORECAST_FREEZE_DATE);
        final CodeValues codeValue = codeValues.get(0);
        codeValue.setCodeValue(forecastFreezeDateString);
        codeValue.setLastChangedDate(LocalDate.now());
        codeValue.setLastChangedBy(SecurityUtils.getCurrentUserLogin());
        codeValuesService.save(codeValue);
    }

    @Override
    @Transactional(readOnly = true)
    public HSSFWorkbook writeForecastOfAllEmployee(final List<Employee> employees) {
        return downloadHelper.writeHoursInWorkbook(getEmployeesForecast(employees));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeForecast> getEmployeesForecast(final List<Employee> employees) {
        final List<EmployeeForecast> employeesForecast = new ArrayList<>();
        for (final Employee employee : employees) {
            final EmployeeForecast employeeForecast = new EmployeeForecast();
            employeeForecast.setId(employee.getId());
            employeeForecast.setAssociateId(employee.getAssociateId());
            employeeForecast.setName(employee.getName());
            employeeForecast.setDomain(employee.getDomain());
            employeeForecast.setProject(getEmployeeProject(employee.getAssociateId()));
            employeeForecast.setEmployeeHours(getForecastedHours(employee.getAssociateId()));
            employeesForecast.add(employeeForecast);
        }
        return employeesForecast;
    }

    private String getEmployeeProject(final Long empId) {
        final List<EmployeeAllocation> activeEmployeeAllocations =
                employeeAllocationService.findActiveEmployeeAllocationsForEmployee(empId);
        if (CollectionUtils.isNotEmpty(activeEmployeeAllocations)) {
            return activeEmployeeAllocations.get(0).getProject();
        }
        return null;
    }

}
