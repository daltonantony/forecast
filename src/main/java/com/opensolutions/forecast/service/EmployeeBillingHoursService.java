package com.opensolutions.forecast.service;

import com.opensolutions.forecast.domain.EmployeeBillingHours;

import java.util.List;

/**
 * Service Interface for managing EmployeeBillingHours.
 */
public interface EmployeeBillingHoursService {

    /**
     * Save a employeeBillingHours.
     * @return the persisted entity
     */
    public EmployeeBillingHours save(EmployeeBillingHours employeeBillingHours);

    /**
     *  get all the employeeBillingHourss.
     *  @return the list of entities
     */
    public List<EmployeeBillingHours> findAll();

    /**
     *  get all the employeeBillingHourss.
     *  @return the list of entities
     */
    public List<EmployeeBillingHours> findComingMonths(Long empId);

    /**
     *  get the "id" employeeBillingHours.
     *  @return the entity
     */
    public EmployeeBillingHours findOne(Long id);

    /**
     *  delete the "id" employeeBillingHours.
     */
    public void delete(Long id);

    /**
     * search for the employeeBillingHours corresponding
     * to the query.
     */
    public List<EmployeeBillingHours> search(String query);
}
