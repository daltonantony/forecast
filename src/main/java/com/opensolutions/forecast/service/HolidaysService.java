package com.opensolutions.forecast.service;

import com.opensolutions.forecast.domain.Holidays;

import java.util.List;

/**
 * Service Interface for managing Holidays.
 */
public interface HolidaysService {

    /**
     * Save a holidays.
     * @return the persisted entity
     */
    public Holidays save(Holidays holidays);

    /**
     *  get all the holidayss.
     *  @return the list of entities
     */
    public List<Holidays> findAll();

    /**
     *  get the "id" holidays.
     *  @return the entity
     */
    public Holidays findOne(Long id);

    /**
     *  delete the "id" holidays.
     */
    public void delete(Long id);

    /**
     * search for the holidays corresponding
     * to the query.
     */
    public List<Holidays> search(String query);
    
    /**
     * Gets all the holidays for the location.
     * @param location location
     * @return holidays for the location
     */
    public List<Holidays> getHolidaysForLocation(String location);
}
