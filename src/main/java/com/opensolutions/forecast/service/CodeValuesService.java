package com.opensolutions.forecast.service;

import com.opensolutions.forecast.domain.CodeValues;

import java.util.List;

/**
 * Service Interface for managing CodeValues.
 */
public interface CodeValuesService {

    /**
     * Save a codeValues.
     * @return the persisted entity
     */
    public CodeValues save(CodeValues codeValues);

    /**
     *  get all the codeValuess.
     *  @return the list of entities
     */
    public List<CodeValues> findAll();

    /**
     *  get the "id" codeValues.
     *  @return the entity
     */
    public CodeValues findOne(Long id);

    /**
     *  delete the "id" codeValues.
     */
    public void delete(Long id);

    /**
     * search for the codeValues corresponding
     * to the query.
     */
    public List<CodeValues> search(String query);
}
