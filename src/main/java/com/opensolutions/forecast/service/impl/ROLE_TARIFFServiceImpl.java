package com.opensolutions.forecast.service.impl;

import com.opensolutions.forecast.service.ROLE_TARIFFService;
import com.opensolutions.forecast.domain.ROLE_TARIFF;
import com.opensolutions.forecast.repository.ROLE_TARIFFRepository;
import com.opensolutions.forecast.repository.search.ROLE_TARIFFSearchRepository;
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
 * Service Implementation for managing ROLE_TARIFF.
 */
@Service
@Transactional
public class ROLE_TARIFFServiceImpl implements ROLE_TARIFFService{

    private final Logger log = LoggerFactory.getLogger(ROLE_TARIFFServiceImpl.class);
    
    @Inject
    private ROLE_TARIFFRepository rOLE_TARIFFRepository;
    
    @Inject
    private ROLE_TARIFFSearchRepository rOLE_TARIFFSearchRepository;
    
    /**
     * Save a rOLE_TARIFF.
     * @return the persisted entity
     */
    public ROLE_TARIFF save(ROLE_TARIFF rOLE_TARIFF) {
        log.debug("Request to save ROLE_TARIFF : {}", rOLE_TARIFF);
        ROLE_TARIFF result = rOLE_TARIFFRepository.save(rOLE_TARIFF);
        rOLE_TARIFFSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the rOLE_TARIFFs.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ROLE_TARIFF> findAll() {
        log.debug("Request to get all ROLE_TARIFFs");
        List<ROLE_TARIFF> result = rOLE_TARIFFRepository.findAll();
        return result;
    }

    /**
     *  get one rOLE_TARIFF by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ROLE_TARIFF findOne(Long id) {
        log.debug("Request to get ROLE_TARIFF : {}", id);
        ROLE_TARIFF rOLE_TARIFF = rOLE_TARIFFRepository.findOne(id);
        return rOLE_TARIFF;
    }

    /**
     *  delete the  rOLE_TARIFF by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete ROLE_TARIFF : {}", id);
        rOLE_TARIFFRepository.delete(id);
        rOLE_TARIFFSearchRepository.delete(id);
    }

    /**
     * search for the rOLE_TARIFF corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<ROLE_TARIFF> search(String query) {
        
        log.debug("REST request to search ROLE_TARIFFs for query {}", query);
        return StreamSupport
            .stream(rOLE_TARIFFSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
