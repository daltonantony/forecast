package com.opensolutions.forecast.web.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.opensolutions.forecast.domain.DaysOfMonth;
import com.opensolutions.forecast.domain.EmployeeForecast;

/**
 * The Class EmployeeHoursAdminDTO.
 */
public class EmployeeHoursAdminDTO {

    private List<EmployeeForecast> employeesForecast;
    private Map<LocalDate, List<DaysOfMonth>> employeeHours;

    /**
     * Gets the employees forecast.
     *
     * @return the employees forecast
     */
    public List<EmployeeForecast> getEmployeesForecast() {
        return employeesForecast;
    }

    /**
     * Sets the employees forecast.
     *
     * @param employeesForecast the new employees forecast
     */
    public void setEmployeesForecast(final List<EmployeeForecast> employeesForecast) {
        this.employeesForecast = employeesForecast;
    }

    /**
     * Gets the employee hours.
     *
     * @return the employee hours
     */
    public Map<LocalDate, List<DaysOfMonth>> getEmployeeHours() {
        return employeeHours;
    }

    /**
     * Sets the employee hours.
     *
     * @param employeeHours the employee hours
     */
    public void setEmployeeHours(final Map<LocalDate, List<DaysOfMonth>> employeeHours) {
        this.employeeHours = employeeHours;
    }

}
