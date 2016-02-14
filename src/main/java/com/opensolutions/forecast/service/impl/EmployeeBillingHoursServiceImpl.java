package com.opensolutions.forecast.service.impl;

import com.opensolutions.forecast.service.EmployeeBillingHoursService;
import com.opensolutions.forecast.domain.EmployeeBillingHours;
import com.opensolutions.forecast.repository.EmployeeBillingHoursRepository;
import com.opensolutions.forecast.repository.search.EmployeeBillingHoursSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EmployeeBillingHours.
 */
@Service
@Transactional
public class EmployeeBillingHoursServiceImpl implements EmployeeBillingHoursService{

    private final Logger log = LoggerFactory.getLogger(EmployeeBillingHoursServiceImpl.class);
    
    @Inject
    private EmployeeBillingHoursRepository employeeBillingHoursRepository;
    
    @Inject
    private EmployeeBillingHoursSearchRepository employeeBillingHoursSearchRepository;
    
    /**
     * Save a employeeBillingHours.
     * @return the persisted entity
     */
    public EmployeeBillingHours save(EmployeeBillingHours employeeBillingHours) {
        log.debug("Request to save EmployeeBillingHours : {}", employeeBillingHours);
        EmployeeBillingHours result = employeeBillingHoursRepository.save(employeeBillingHours);
        employeeBillingHoursSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the employeeBillingHourss.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<EmployeeBillingHours> findAll() {
        log.debug("Request to get all EmployeeBillingHourss");
        List<EmployeeBillingHours> result = employeeBillingHoursRepository.findAll();
        return result;
    }

    /**
     *  get one employeeBillingHours by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EmployeeBillingHours findOne(Long id) {
        log.debug("Request to get EmployeeBillingHours : {}", id);
        EmployeeBillingHours employeeBillingHours = employeeBillingHoursRepository.findOne(id);
        return employeeBillingHours;
    }

    /**
     *  delete the  employeeBillingHours by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeeBillingHours : {}", id);
        employeeBillingHoursRepository.delete(id);
        employeeBillingHoursSearchRepository.delete(id);
    }

    /**
     * search for the employeeBillingHours corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<EmployeeBillingHours> search(String query) {
        
        log.debug("REST request to search EmployeeBillingHourss for query {}", query);
        return StreamSupport
            .stream(employeeBillingHoursSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
