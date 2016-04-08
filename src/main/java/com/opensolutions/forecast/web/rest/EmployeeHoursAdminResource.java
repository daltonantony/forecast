package com.opensolutions.forecast.web.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.service.EmployeeHoursService;
import com.opensolutions.forecast.service.EmployeeService;
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
    private static final String DOWNLOAD_FORECAST = "/downloadForecast";

    @Inject
    private EmployeeService employeeService;
    @Inject
    private EmployeeHoursService employeeHoursService;

    @RequestMapping(value = SET_FORECAST_FREEZE_DATE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public EmployeeHoursAdminDTO showSetForecastFreezeDate() {
        log.debug("REST request to show the view to set the forecast freeze date");
        final EmployeeHoursAdminDTO dto = new EmployeeHoursAdminDTO();
        dto.setForecastFreezeDate(employeeHoursService.getForecastFreezeDate());
        return dto;
    }

    @RequestMapping(value = SET_FORECAST_FREEZE_DATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> setForecastFreezeDate(@RequestBody final EmployeeHoursAdminDTO dto) {
        log.debug("REST request to set the forecast freeze date");
        employeeHoursService.setForecastFreezeDate(dto.getForecastFreezeDate());
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Forecast Freeze Date Set", "")).build();
    }

    @RequestMapping(value = DOWNLOAD_FORECAST, method = RequestMethod.GET, produces = "application/vnd.ms-excel")
    @Timed
    public ResponseEntity downloadForecastHours(final HttpServletResponse response) {
        log.debug("REST request to download the forecast hours");
        final List<Employee> employees = employeeService.findAll();
        final HSSFWorkbook workbook = employeeHoursService.getForecastOfAllEmployee(employees);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-disposition", "attachment; filename=EmpHrs.xls");
        responseHeaders.add("Content-Type", "application/vnd.ms-excel");
        
        final ResponseEntity a = new ResponseEntity(out, responseHeaders, HttpStatus.OK);

        return a;
    }

}
