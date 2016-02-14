package com.opensolutions.forecast.service;

import com.opensolutions.forecast.domain.EmployeeHolidays;

import java.util.List;

/**
 * Service Interface for managing EmployeeHolidays.
 */
public interface EmployeeHolidaysService {

    /**
     * Save a employeeHolidays.
     * @return the persisted entity
     */
    public EmployeeHolidays save(EmployeeHolidays employeeHolidays);

    /**
     *  get all the employeeHolidayss.
     *  @return the list of entities
     */
    public List<EmployeeHolidays> findAll();

    /**
     *  get the "id" employeeHolidays.
     *  @return the entity
     */
    public EmployeeHolidays findOne(Long id);

    /**
     *  delete the "id" employeeHolidays.
     */
    public void delete(Long id);

    /**
     * search for the employeeHolidays corresponding
     * to the query.
     */
    public List<EmployeeHolidays> search(String query);
}
