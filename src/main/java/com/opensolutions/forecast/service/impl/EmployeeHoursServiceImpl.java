package com.opensolutions.forecast.service.impl;

import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.domain.EmployeeHours;
import com.opensolutions.forecast.repository.EmployeeHoursRepository;
import com.opensolutions.forecast.repository.EmployeeRepository;
import com.opensolutions.forecast.repository.search.EmployeeHoursSearchRepository;
import com.opensolutions.forecast.security.SecurityUtils;
import com.opensolutions.forecast.service.EmployeeHoursService;
import com.opensolutions.forecast.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing EmployeeHours.
 */
@Service
@Transactional
public class EmployeeHoursServiceImpl implements EmployeeHoursService {

    private final Logger log = LoggerFactory.getLogger(EmployeeHoursServiceImpl.class);

    @Inject
    private EmployeeHoursRepository employeeHoursRepository;

    @Inject
    private EmployeeHoursSearchRepository employeeHoursSearchRepository;

    @Inject
    private EmployeeRepository employeeRepository;
    
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
    public Map<String, List<EmployeeHours>> getEmployeeHoursForComingMonths() {
        final Map<String, List<EmployeeHours>> employeeHoursMap = new LinkedHashMap<>();
        final Long empId = Long.valueOf(SecurityUtils.getCurrentUserLogin());
        log.debug("REST request to get all Employee Hours for Coming Months for: {}", empId);
		final Employee employee = employeeService.getEmployeeForAssociateId(empId);
		log.debug("Retrieved employee: {}", employee.getName());
        employeeHoursMap.put(LocalDate.now().plusMonths(1).getMonth().toString(), getEmployeeHoursList(employee, 1));
        employeeHoursMap.put(LocalDate.now().plusMonths(2).getMonth().toString(), getEmployeeHoursList(employee, 2));
        employeeHoursMap.put(LocalDate.now().plusMonths(3).getMonth().toString(), getEmployeeHoursList(employee, 3));
        return employeeHoursMap;
    }

    private List<EmployeeHours> getEmployeeHoursList(final Employee employee, final int monthAdd) {
        final EmployeeHours working = getEmployeeHours(employee, LocalDate.now().plusMonths(monthAdd), "Working");
        final EmployeeHours personal = getEmployeeHours(employee, LocalDate.now().plusMonths(monthAdd), "Personal");
        final EmployeeHours training = getEmployeeHours(employee, LocalDate.now().plusMonths(monthAdd), "Training");
        return Arrays.asList(working, personal, training);
    }

    private EmployeeHours getEmployeeHours(final Employee employee, final LocalDate forecastDate, final String type) {
        final EmployeeHours hours = new EmployeeHours();
        hours.setEmployee(employee);
        hours.setWeek1(40);
        hours.setWeek2(40);
        hours.setWeek3(40);
        hours.setWeek4(40);
        hours.setWeek5(40);
        if (!"Working".equals(type)) {
            hours.setWeek1(0);
            hours.setWeek2(0);
            hours.setWeek3(0);
            hours.setWeek4(0);
            hours.setWeek5(0);
        }
        hours.setType(type);
        hours.setCreatedDate(LocalDate.now());
        hours.setLastChangedDate(LocalDate.now());
        hours.setForecastDate(forecastDate);
        hours.setLastChangedBy("admin");
        return hours;
    }
}
