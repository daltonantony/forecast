package com.opensolutions.forecast.service.impl;

import com.opensolutions.forecast.service.HolidaysService;
import com.opensolutions.forecast.domain.Holidays;
import com.opensolutions.forecast.repository.HolidaysRepository;
import com.opensolutions.forecast.repository.search.HolidaysSearchRepository;
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
 * Service Implementation for managing Holidays.
 */
@Service
@Transactional
public class HolidaysServiceImpl implements HolidaysService{

    private final Logger log = LoggerFactory.getLogger(HolidaysServiceImpl.class);
    
    @Inject
    private HolidaysRepository holidaysRepository;
    
    @Inject
    private HolidaysSearchRepository holidaysSearchRepository;
    
    /**
     * Save a holidays.
     * @return the persisted entity
     */
    public Holidays save(Holidays holidays) {
        log.debug("Request to save Holidays : {}", holidays);
        Holidays result = holidaysRepository.save(holidays);
        holidaysSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the holidayss.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Holidays> findAll() {
        log.debug("Request to get all Holidayss");
        List<Holidays> result = holidaysRepository.findAll();
        return result;
    }

    /**
     *  get one holidays by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Holidays findOne(Long id) {
        log.debug("Request to get Holidays : {}", id);
        Holidays holidays = holidaysRepository.findOne(id);
        return holidays;
    }

    /**
     *  delete the  holidays by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Holidays : {}", id);
        holidaysRepository.delete(id);
        holidaysSearchRepository.delete(id);
    }

    /**
     * search for the holidays corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Holidays> search(String query) {
        
        log.debug("REST request to search Holidayss for query {}", query);
        return StreamSupport
            .stream(holidaysSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
