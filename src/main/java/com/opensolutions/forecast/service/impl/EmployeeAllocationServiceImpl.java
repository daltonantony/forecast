package com.opensolutions.forecast.service.impl;

import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.repository.EmployeeRepository;
import com.opensolutions.forecast.security.SecurityUtils;
import com.opensolutions.forecast.service.EmployeeAllocationService;
import com.opensolutions.forecast.domain.EmployeeAllocation;
import com.opensolutions.forecast.repository.EmployeeAllocationRepository;
import com.opensolutions.forecast.repository.search.EmployeeAllocationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EmployeeAllocation.
 */
@Service
@Transactional
public class EmployeeAllocationServiceImpl implements EmployeeAllocationService{

    private final Logger log = LoggerFactory.getLogger(EmployeeAllocationServiceImpl.class);

    @Inject
    private EmployeeAllocationRepository employeeAllocationRepository;

    @Inject
    private EmployeeAllocationSearchRepository employeeAllocationSearchRepository;

    @Inject
    private EmployeeRepository employeeRepository;

    /**
     * Save a employeeAllocation.
     * @return the persisted entity
     */
    public EmployeeAllocation save(EmployeeAllocation employeeAllocation) {
        employeeAllocation.setLastChangedby(SecurityUtils.getCurrentUser().getUsername());
        employeeAllocation.setLastChangedDate(LocalDate.now());
        log.debug("Request to save EmployeeAllocation : {}", employeeAllocation);
        EmployeeAllocation result = employeeAllocationRepository.save(employeeAllocation);
        employeeAllocationSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the employeeAllocations.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EmployeeAllocation> findAll() {
        log.debug("Request to get all EmployeeAllocations");
        List<EmployeeAllocation> result = employeeAllocationRepository.findAll();
        return result;
    }

    /**
     *  get one employeeAllocation by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EmployeeAllocation findOne(Long id) {
        log.debug("Request to get EmployeeAllocation : {}", id);
        EmployeeAllocation employeeAllocation = employeeAllocationRepository.findOne(id);
        return employeeAllocation;
    }


    /**
     *  get one employeeAllocation by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Set<EmployeeAllocation> findAnEmployeeAllocations(Long empId) {
        log.debug("Request to get EmployeeAllocation For EmpId : {}", empId);
        final Employee employee = employeeRepository.findOne(empId);
        return employee.getEmployeeAllocations();
    }

    /**
     *  delete the  employeeAllocation by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeeAllocation : {}", id);
        employeeAllocationRepository.delete(id);
        employeeAllocationSearchRepository.delete(id);
    }

    /**
     * search for the employeeAllocation corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<EmployeeAllocation> search(String query) {

        log.debug("REST request to search EmployeeAllocations for query {}", query);
        return StreamSupport
            .stream(employeeAllocationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
