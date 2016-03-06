package com.opensolutions.forecast.service;

import java.util.List;

import com.opensolutions.forecast.domain.CodeValues;

/**
 * Service Interface for managing CodeValues.
 */
public interface CodeValuesService {

    /**
     * Save a codeValues.
     * @return the persisted entity
     */
    CodeValues save(CodeValues codeValues);

    /**
     *  get all the codeValuess.
     *  @return the list of entities
     */
    List<CodeValues> findAll();

    /**
     *  get the "id" codeValues.
     *  @return the entity
     */
    CodeValues findOne(Long id);

    /**
     *  delete the "id" codeValues.
     */
    void delete(Long id);

    /**
     * search for the codeValues corresponding
     * to the query.
     */
    List<CodeValues> search(String query);

    /**
     * search for the active codeValues corresponding
     * to the codeType.
     */
    List<CodeValues> searchActiveCodeValues(String codeType);

}
