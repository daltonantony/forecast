package com.opensolutions.forecast.web.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
import com.opensolutions.forecast.web.rest.dto.AdminDTO;
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
    private static final String DOWNLOAD_FORECAST_FOR_ALL = "/downloadForecastForAll";
    private static final String SHOW_FORECAST_FOR_ALL = "/showForecastForAll";
    private static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";

    @Inject
    private EmployeeService employeeService;
    @Inject
    private EmployeeHoursService employeeHoursService;

    @RequestMapping(value = SET_FORECAST_FREEZE_DATE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public AdminDTO showSetForecastFreezeDate() {
        log.debug("REST request to show the view to set the forecast freeze date");
        final AdminDTO dto = new AdminDTO();
        dto.setForecastFreezeDate(employeeHoursService.getForecastFreezeDate());
        return dto;
    }

    @RequestMapping(value = SET_FORECAST_FREEZE_DATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> setForecastFreezeDate(@RequestBody final AdminDTO dto) {
        log.debug("REST request to set the forecast freeze date");
        employeeHoursService.setForecastFreezeDate(dto.getForecastFreezeDate());
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Forecast Freeze Date Set", "")).build();
    }

    @RequestMapping(value = DOWNLOAD_FORECAST_FOR_ALL, method = RequestMethod.GET, produces = APPLICATION_VND_MS_EXCEL)
    @Timed
    public ResponseEntity<byte[]> downloadForecastHoursForAllEmployees() {
        log.debug("REST request to download the forecast hours");
        final List<Employee> employees = employeeService.findAll();
        final HSSFWorkbook workbook = employeeHoursService.writeForecastOfAllEmployee(employees);

        final HttpHeaders headers = HeaderUtil.createAlert("Download complete!", "");
        headers.add("Content-Disposition", "attachment; filename=ForecastHours.xls");
        headers.add("Content-Type", APPLICATION_VND_MS_EXCEL);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        final byte[] bytes = baos.toByteArray();

        // TODO: exception handling
        return ResponseEntity.ok().headers(headers).contentLength(bytes.length).body(bytes);
    }

    @RequestMapping(value = SHOW_FORECAST_FOR_ALL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public EmployeeHoursAdminDTO getForecastHoursForAllEmployees() {
        log.debug("REST request to get the Employee Hours for Coming Months for all employees");
        final List<Employee> employees = employeeService.findAll();
        final EmployeeHoursAdminDTO dto = new EmployeeHoursAdminDTO();
        dto.setEmployeesForecast(employeeHoursService.getEmployeesForecast(employees));
        return dto;
    }

}
