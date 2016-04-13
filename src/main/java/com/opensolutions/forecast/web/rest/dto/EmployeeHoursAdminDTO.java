package com.opensolutions.forecast.web.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.opensolutions.forecast.domain.DaysOfMonth;
import com.opensolutions.forecast.domain.Employee;

public class EmployeeHoursAdminDTO {

    private LocalDate forecastFreezeDate;
    private Map<Employee, Map<LocalDate, List<DaysOfMonth>>> allEmployeeWithForecast;
    private Map<LocalDate, List<DaysOfMonth>> employeeHours;

    public LocalDate getForecastFreezeDate() {
        return forecastFreezeDate;
    }

    public void setForecastFreezeDate(final LocalDate forecastFreezeDate) {
        this.forecastFreezeDate = forecastFreezeDate;
    }

    @Override
    public String toString() {
        return "EmployeeHoursAdminDTO{forecastFreezeDate=" + forecastFreezeDate + "}";
    }

    public Map<Employee, Map<LocalDate, List<DaysOfMonth>>> getAllEmployeeWithForecast() {
        return allEmployeeWithForecast;
    }

    public void setAllEmployeeWithForecast(final Map<Employee, Map<LocalDate, List<DaysOfMonth>>> allEmployeeWithForecast) {
        this.allEmployeeWithForecast = allEmployeeWithForecast;
    }

    public Map<LocalDate, List<DaysOfMonth>> getEmployeeHours() {
        return employeeHours;
    }

    public void setEmployeeHours(final Map<LocalDate, List<DaysOfMonth>> employeeHours) {
        this.employeeHours = employeeHours;
    }

}
