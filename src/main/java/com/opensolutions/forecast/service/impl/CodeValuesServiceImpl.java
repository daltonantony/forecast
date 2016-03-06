package com.opensolutions.forecast.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensolutions.forecast.domain.CodeValues;
import com.opensolutions.forecast.repository.CodeValuesRepository;
import com.opensolutions.forecast.repository.search.CodeValuesSearchRepository;
import com.opensolutions.forecast.security.SecurityUtils;
import com.opensolutions.forecast.service.CodeValuesService;

/**
 * Service Implementation for managing CodeValues.
 */
@Service
@Transactional
public class CodeValuesServiceImpl implements CodeValuesService {

    private final Logger log = LoggerFactory.getLogger(CodeValuesServiceImpl.class);

    @Inject
    private CodeValuesRepository codeValuesRepository;

    @Inject
    private CodeValuesSearchRepository codeValuesSearchRepository;

    /**
     * Save a codeValues.
     * @return the persisted entity
     */
    @Override
	public CodeValues save(final CodeValues codeValues) {
        codeValues.setLastChangedBy(SecurityUtils.getCurrentUserLogin());
        codeValues.setLastChangedDate(LocalDate.now());
        log.debug("Request to save CodeValues : {}", codeValues);
        final CodeValues result = codeValuesRepository.save(codeValues);
        codeValuesSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the codeValuess.
     *  @return the list of entities
     */
    @Override
	@Transactional(readOnly = true)
    public List<CodeValues> findAll() {
        log.debug("Request to get all CodeValuess");
        final List<CodeValues> result = codeValuesRepository.findAll();
        return result;
    }

    /**
     *  get one codeValues by id.
     *  @return the entity
     */
    @Override
	@Transactional(readOnly = true)
    public CodeValues findOne(final Long id) {
        log.debug("Request to get CodeValues : {}", id);
        final CodeValues codeValues = codeValuesRepository.findOne(id);
        return codeValues;
    }

    /**
     *  delete the  codeValues by id.
     */
    @Override
	public void delete(final Long id) {
        log.debug("Request to delete CodeValues : {}", id);
        codeValuesRepository.delete(id);
        codeValuesSearchRepository.delete(id);
    }

    /**
     * search for the codeValues corresponding
     * to the query.
     */
    @Override
	@Transactional(readOnly = true)
    public List<CodeValues> search(final String query) {
        log.debug("REST request to search CodeValuess for query {}", query);
        final List<CodeValues> queriedCodeValues = new ArrayList<>();
        final List<CodeValues> allCodeValues = findAll();
        for (final CodeValues codeValues : allCodeValues) {
			if (codeValues.getCodeType().equalsIgnoreCase(query)) {
				queriedCodeValues.add(codeValues);
			}
		}
        return queriedCodeValues;
    }

    /**
     * search for the active codeValues corresponding
     * to the codeType.
     */
    @Override
	@Transactional(readOnly = true)
    public List<CodeValues> searchActiveCodeValues(final String codeType) {
        log.debug("REST request to search Active CodeValues for query {}", codeType);
        final List<CodeValues> queriedActiveCodeValues = new ArrayList<>();
        final List<CodeValues> allCodeValues = findAll();
        for (final CodeValues codeValues : allCodeValues) {
			if (codeValues.getCodeType().equalsIgnoreCase(codeType) && isCodeValueActive(codeValues)) {
				queriedActiveCodeValues.add(codeValues);
			}
		}
        return queriedActiveCodeValues;
    }

    private boolean isCodeValueActive(final CodeValues codeValues) {
    	final boolean isEffectiveDateBeforeNow = codeValues.getEffectiveDate().isBefore(LocalDate.now());
    	final LocalDate expiryDate = codeValues.getExpiryDate();
    	final boolean isExpiryDateEitherNullOrAfterNow =
    			expiryDate == null || expiryDate.isAfter(LocalDate.now());
    	return isEffectiveDateBeforeNow && isExpiryDateEitherNullOrAfterNow;
    }

}
