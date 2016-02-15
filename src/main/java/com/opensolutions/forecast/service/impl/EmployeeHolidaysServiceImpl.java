package com.opensolutions.forecast.service.impl;

import com.opensolutions.forecast.service.EmployeeHolidaysService;
import com.opensolutions.forecast.domain.EmployeeHolidays;
import com.opensolutions.forecast.repository.EmployeeHolidaysRepository;
import com.opensolutions.forecast.repository.search.EmployeeHolidaysSearchRepository;
import com.opensolutions.forecast.security.SecurityUtils;

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
 * Service Implementation for managing EmployeeHolidays.
 */
@Service
@Transactional
public class EmployeeHolidaysServiceImpl implements EmployeeHolidaysService{

    private final Logger log = LoggerFactory.getLogger(EmployeeHolidaysServiceImpl.class);
    
    @Inject
    private EmployeeHolidaysRepository employeeHolidaysRepository;
    
    @Inject
    private EmployeeHolidaysSearchRepository employeeHolidaysSearchRepository;
    
    /**
     * Save a employeeHolidays.
     * @return the persisted entity
     */
    public EmployeeHolidays save(EmployeeHolidays employeeHolidays) {
    	employeeHolidays.setLastChangedBy(SecurityUtils.getCurrentUserLogin());
    	employeeHolidays.setLastChangedDate(LocalDate.now());
        log.debug("Request to save EmployeeHolidays : {}", employeeHolidays);
        EmployeeHolidays result = employeeHolidaysRepository.save(employeeHolidays);
        employeeHolidaysSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the employeeHolidayss.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<EmployeeHolidays> findAll() {
        log.debug("Request to get all EmployeeHolidayss");
        List<EmployeeHolidays> result = employeeHolidaysRepository.findAll();
        return result;
    }

    /**
     *  get one employeeHolidays by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EmployeeHolidays findOne(Long id) {
        log.debug("Request to get EmployeeHolidays : {}", id);
        EmployeeHolidays employeeHolidays = employeeHolidaysRepository.findOne(id);
        return employeeHolidays;
    }

    /**
     *  delete the  employeeHolidays by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeeHolidays : {}", id);
        employeeHolidaysRepository.delete(id);
        employeeHolidaysSearchRepository.delete(id);
    }

    /**
     * search for the employeeHolidays corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<EmployeeHolidays> search(String query) {
        
        log.debug("REST request to search EmployeeHolidayss for query {}", query);
        return StreamSupport
            .stream(employeeHolidaysSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
