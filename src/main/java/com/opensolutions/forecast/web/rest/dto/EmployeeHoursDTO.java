package com.opensolutions.forecast.web.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.opensolutions.forecast.domain.DaysOfMonth;

public class EmployeeHoursDTO {

	private Map<LocalDate, List<DaysOfMonth>> employeeHoursForComingMonths;
	private boolean forecastFreezePeriod;

	public Map<LocalDate, List<DaysOfMonth>> getEmployeeHoursForComingMonths() {
		return employeeHoursForComingMonths;
	}

	public void setEmployeeHoursForComingMonths(final Map<LocalDate, List<DaysOfMonth>> employeeHoursForComingMonths) {
		this.employeeHoursForComingMonths = employeeHoursForComingMonths;
	}

	public boolean getForecastFreezePeriod() {
		return forecastFreezePeriod;
	}

	public void setForecastFreezePeriod(final boolean forecastFreezePeriod) {
		this.forecastFreezePeriod = forecastFreezePeriod;
	}

}
