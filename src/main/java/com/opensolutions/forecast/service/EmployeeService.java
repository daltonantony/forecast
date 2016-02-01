package com.opensolutions.forecast.service;

import com.opensolutions.forecast.domain.Employee;

import java.util.List;

/**
 * Service Interface for managing Employee.
 */
public interface EmployeeService {

    /**
     * Save a employee.
     * @return the persisted entity
     */
    public Employee save(Employee employee);

    /**
     *  get all the employees.
     *  @return the list of entities
     */
    public List<Employee> findAll();

    /**
     *  get the "id" employee.
     *  @return the entity
     */
    public Employee findOne(Long id);

    /**
     *  delete the "id" employee.
     */
    public void delete(Long id);

    /**
     * search for the employee corresponding
     * to the query.
     */
    public List<Employee> search(String query);
}
