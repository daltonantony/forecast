package com.opensolutions.forecast.service;

import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.domain.EmployeeHours;

import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing EmployeeHours.
 */
public interface EmployeeHoursService {

    /**
     * Save a employeeHours.
     * @return the persisted entity
     */
    public EmployeeHours save(EmployeeHours employeeHours);

    /**
     *  get all the employeeHourss.
     *  @return the list of entities
     */
    public List<EmployeeHours> findAll();

    /**
     *  get the "id" employeeHours.
     *  @return the entity
     */
    public EmployeeHours findOne(Long id);

    /**
     *  delete the "id" employeeHours.
     */
    public void delete(Long id);

    /**
     * search for the employeeHours corresponding
     * to the query.
     */
    public List<EmployeeHours> search(String query);

    public Map<String, List<EmployeeHours>> findComingMonthHours(Long empId);
}