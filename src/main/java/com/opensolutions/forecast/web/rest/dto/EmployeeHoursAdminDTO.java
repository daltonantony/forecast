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
    private String employeeIdSelected;
    private String employeeNameSelected;

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

    /**
     * Gets the employee id selected.
     *
     * @return the employee id selected
     */
    public String getEmployeeIdSelected() {
        return employeeIdSelected;
    }

    /**
     * Sets the employee id selected.
     *
     * @param employeeIdSelected the new employee id selected
     */
    public void setEmployeeIdSelected(final String employeeIdSelected) {
        this.employeeIdSelected = employeeIdSelected;
    }

    /**
     * Gets the employee name selected.
     *
     * @return the employee name selected
     */
    public String getEmployeeNameSelected() {
        return employeeNameSelected;
    }

    /**
     * Sets the employee name selected.
     *
     * @param employeeNameSelected the new employee name selected
     */
    public void setEmployeeNameSelected(final String employeeNameSelected) {
        this.employeeNameSelected = employeeNameSelected;
    }

}
