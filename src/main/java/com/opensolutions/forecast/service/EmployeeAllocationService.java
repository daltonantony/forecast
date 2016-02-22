package com.opensolutions.forecast.service;

import com.opensolutions.forecast.domain.EmployeeAllocation;

import java.util.List;
import java.util.Set;

/**
 * Service Interface for managing EmployeeAllocation.
 */
public interface EmployeeAllocationService {

    /**
     * Save a employeeAllocation.
     * @return the persisted entity
     */
    public EmployeeAllocation save(EmployeeAllocation employeeAllocation);

    /**
     *  get all the employeeAllocations.
     *  @return the list of entities
     */
    public List<EmployeeAllocation> findAll();

    /**
     *  get the "id" employeeAllocation.
     *  @return the entity
     */
    public EmployeeAllocation findOne(Long id);

    /**
     * Find all employee allocations for the employee.
     * @param empId employee id
     * @return Employee Allocations for employee id
     */
    public Set<EmployeeAllocation> findAnEmployeeAllocations(Long empId);
    
    /**
     * Find all employee allocations for the employee.
     * @param empId employee id
     * @return All Employee Allocations for employee id
     */
    public List<EmployeeAllocation> findAllEmployeeAllocationsForEmployee(Long empId);

    /**
     * Find active employee allocations for the employee.
     * @param empId employee id
     * @return Active Employee Allocations for employee id
     */
    public List<EmployeeAllocation> findActiveEmployeeAllocationsForEmployee(Long empId);

    /**
     *  delete the "id" employeeAllocation.
     */
    public void delete(Long id);

    /**
     * search for the employeeAllocation corresponding
     * to the query.
     */
    public List<EmployeeAllocation> search(String query);
}
