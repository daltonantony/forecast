package com.opensolutions.forecast.service.impl;

import com.opensolutions.forecast.security.SecurityUtils;
import com.opensolutions.forecast.service.CodeValuesService;
import com.opensolutions.forecast.domain.CodeValues;
import com.opensolutions.forecast.repository.CodeValuesRepository;
import com.opensolutions.forecast.repository.search.CodeValuesSearchRepository;
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
 * Service Implementation for managing CodeValues.
 */
@Service
@Transactional
public class CodeValuesServiceImpl implements CodeValuesService{

    private final Logger log = LoggerFactory.getLogger(CodeValuesServiceImpl.class);

    @Inject
    private CodeValuesRepository codeValuesRepository;

    @Inject
    private CodeValuesSearchRepository codeValuesSearchRepository;

    /**
     * Save a codeValues.
     * @return the persisted entity
     */
    public CodeValues save(CodeValues codeValues) {
        codeValues.setLastChangedBy(SecurityUtils.getCurrentUser().getUsername());
        codeValues.setLastChangedDate(LocalDate.now());
        log.debug("Request to save CodeValues : {}", codeValues);
        CodeValues result = codeValuesRepository.save(codeValues);
        codeValuesSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the codeValuess.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CodeValues> findAll() {
        log.debug("Request to get all CodeValuess");
        List<CodeValues> result = codeValuesRepository.findAll();
        return result;
    }

    /**
     *  get one codeValues by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CodeValues findOne(Long id) {
        log.debug("Request to get CodeValues : {}", id);
        CodeValues codeValues = codeValuesRepository.findOne(id);
        return codeValues;
    }

    /**
     *  delete the  codeValues by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete CodeValues : {}", id);
        codeValuesRepository.delete(id);
        codeValuesSearchRepository.delete(id);
    }

    /**
     * search for the codeValues corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<CodeValues> search(String query) {

        log.debug("REST request to search CodeValuess for query {}", query);
        return StreamSupport
            .stream(codeValuesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
