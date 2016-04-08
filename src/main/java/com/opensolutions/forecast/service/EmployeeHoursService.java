package com.opensolutions.forecast.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.opensolutions.forecast.domain.DaysOfMonth;
import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.domain.EmployeeHours;

/**
 * Service Interface for managing EmployeeHours.
 */
public interface EmployeeHoursService {

    /**
     * Save a employeeHours.
     * 
     * @return the persisted entity
     */
    EmployeeHours save(EmployeeHours employeeHours);

    /**
     * get all the employeeHourss.
     * 
     * @return the list of entities
     */
    List<EmployeeHours> findAll();

    /**
     * get the "id" employeeHours.
     * 
     * @return the entity
     */
    EmployeeHours findOne(Long id);

    /**
     * delete the "id" employeeHours.
     */
    void delete(Long id);

    /**
     * search for the employeeHours corresponding to the query.
     */
    List<EmployeeHours> search(String query);

    /**
     * @return Employee Hours for Coming Months
     */
    Map<LocalDate, List<DaysOfMonth>> getEmployeeHoursForComingMonths();

    /**
     * Saves employee hours for coming months.
     * 
     * @param employeeHoursForComingMonths the employee hours for coming months
     * @return Employee
     */
    Employee saveEmployeeHoursForComingMonths(Map<LocalDate, List<DaysOfMonth>> employeeHoursForComingMonths);

    /**
     * @return Employee Hours for Previous Months
     */
    Map<LocalDate, List<EmployeeHours>> getEmployeeHoursForPreviousMonths();

    /**
     * @return whether the current day is part of the "forecast freeze period"
     */
    boolean isForecastFreezePeriod();

    /**
     * @return the Forecast Freeze Date
     */
    LocalDate getForecastFreezeDate();

    /**
     * @param forecastFreezeDate the Forecast Freeze Date to set
     */
    void setForecastFreezeDate(LocalDate forecastFreezeDate);

    /**
     * Get forecast of all employees in Excel.
     * 
     * @param employees the employee details of all employees
     * @return the Employee and employee hours
     */
    HSSFWorkbook getForecastOfAllEmployee(List<Employee> employees);
}
