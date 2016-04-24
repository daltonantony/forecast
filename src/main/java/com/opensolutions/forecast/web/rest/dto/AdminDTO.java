package com.opensolutions.forecast.web.rest.dto;

import java.time.LocalDate;

public class AdminDTO {

    private LocalDate forecastFreezeDate;

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

}
