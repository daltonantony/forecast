package com.opensolutions.forecast.service;

import com.opensolutions.forecast.domain.EmployeeAllocation;

import java.util.List;

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
     *  delete the "id" employeeAllocation.
     */
    public void delete(Long id);

    /**
     * search for the employeeAllocation corresponding
     * to the query.
     */
    public List<EmployeeAllocation> search(String query);
}
