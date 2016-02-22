package com.opensolutions.forecast.service;

import com.opensolutions.forecast.domain.DaysOfMonth;
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

    /**
     * @return Employee Hours for Coming Months
     */
    public Map<String, List<DaysOfMonth>> getEmployeeHoursForComingMonths();
    
    /**
     * Saves employee hours for coming months.
     * @param employeeHoursForComingMonths
     * @return Employee
     */
    public Employee saveEmployeeHoursForComingMonths(Map<String, List<DaysOfMonth>> employeeHoursForComingMonths);

}
