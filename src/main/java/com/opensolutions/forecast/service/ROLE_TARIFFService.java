package com.opensolutions.forecast.service;

import com.opensolutions.forecast.domain.ROLE_TARIFF;

import java.util.List;

/**
 * Service Interface for managing ROLE_TARIFF.
 */
public interface ROLE_TARIFFService {

    /**
     * Save a rOLE_TARIFF.
     * @return the persisted entity
     */
    public ROLE_TARIFF save(ROLE_TARIFF rOLE_TARIFF);

    /**
     *  get all the rOLE_TARIFFs.
     *  @return the list of entities
     */
    public List<ROLE_TARIFF> findAll();

    /**
     *  get the "id" rOLE_TARIFF.
     *  @return the entity
     */
    public ROLE_TARIFF findOne(Long id);

    /**
     *  delete the "id" rOLE_TARIFF.
     */
    public void delete(Long id);

    /**
     * search for the rOLE_TARIFF corresponding
     * to the query.
     */
    public List<ROLE_TARIFF> search(String query);
}
