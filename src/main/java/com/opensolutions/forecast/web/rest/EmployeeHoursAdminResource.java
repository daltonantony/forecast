package com.opensolutions.forecast.web.rest;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.opensolutions.forecast.service.EmployeeHoursService;
import com.opensolutions.forecast.web.rest.dto.EmployeeHoursAdminDTO;
import com.opensolutions.forecast.web.rest.util.HeaderUtil;

/**
 * REST controller for managing EmployeeHours Administration.
 */
@RestController
@RequestMapping("/api")
public class EmployeeHoursAdminResource {

	private final Logger log = LoggerFactory.getLogger(EmployeeHoursAdminResource.class);
	private static final String SET_FORECAST_FREEZE_DATE = "/setForecastFreezeDate";

    @Inject
    private EmployeeHoursService employeeHoursService;

    @RequestMapping(value = SET_FORECAST_FREEZE_DATE,
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public EmployeeHoursAdminDTO showSetForecastFreezeDate() {
        log.debug("REST request to show the view to set the forecast freeze date");
        final LocalDate forecastFreezeDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).minusDays(5);
        final EmployeeHoursAdminDTO dto = new EmployeeHoursAdminDTO();
        dto.setForecastFreezeDate(forecastFreezeDate);
		return dto;
    }

    @RequestMapping(value = SET_FORECAST_FREEZE_DATE,
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> setForecastFreezeDate(@RequestBody final EmployeeHoursAdminDTO dto) {
        log.debug("REST request to set the forecast freeze date");
        log.debug("Forecast Freeze Date to be set as {}", dto.getForecastFreezeDate());
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Forecast Freeze Date Set", "")).build();
    }

}
