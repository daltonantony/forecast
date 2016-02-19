package com.opensolutions.forecast.service.impl;

import com.opensolutions.forecast.security.SecurityUtils;
import com.opensolutions.forecast.service.EmployeeService;
import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.repository.EmployeeRepository;
import com.opensolutions.forecast.repository.search.EmployeeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Employee.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeSearchRepository employeeSearchRepository;

    /**
     * Save a employee.
     * @return the persisted entity
     */
    public Employee save(Employee employee) {
        employee.setLastChangedBy(SecurityUtils.getCurrentUser().getUsername());
        employee.setLastChangedDate(LocalDate.now());
        log.debug("Request to save Employee : {}", employee);
        Employee result = employeeRepository.save(employee);
        employeeSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the employees.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        log.debug("Request to get all Employees");
        List<Employee> result = employeeRepository.findAll();
        return result;
    }

    /**
     *  get one employee by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Employee findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        Employee employee = employeeRepository.findOne(id);
        return employee;
    }

    /**
     *  delete the  employee by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.delete(id);
        employeeSearchRepository.delete(id);
    }

    /**
     * search for the employee corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<Employee> search(String query) {

        log.debug("REST request to search Employees for query {}", query);
        return StreamSupport
            .stream(employeeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

	@Override
	public Employee getEmployeeForAssociateId(Long associateId) {
		final List<Employee> employees = findAll();
		for (Employee employee : employees) {
			if (employee.getAssociateId().equals(associateId)) {
				return employee;
			}
		}
		return null;
	}
}
