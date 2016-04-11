package com.opensolutions.forecast.web.rest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String DOWNLOAD_FORECAST_FOR_ALL = "/downloadForecastForAll";
    private static final String SHOW_FORECAST_FOR_ALL = "/showForecastForAll";

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

    @RequestMapping(value = DOWNLOAD_FORECAST_FOR_ALL, method = RequestMethod.GET, produces = "application/vnd.ms-excel")
    @Timed
    public ResponseEntity<Void> downloadForecastHoursForAllEmployees() {
        log.debug("REST request to download the forecast hours");
        final List<Employee> employees = employeeService.findAll();
        final HSSFWorkbook workbook = employeeHoursService.writeForecastOfAllEmployee(employees);
        // OutputStream out = null;
        try {
            // out = response.getOutputStream();
            // workbook.write(out);
            // out.flush();
            final FileOutputStream file = new FileOutputStream("C:/Users/Public/Downloads/Forecast.xls");
            workbook.write(file);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        /*
         * final HttpHeaders responseHeaders = new HttpHeaders();
         * responseHeaders.add("Cache-Control","must-revalidate"); responseHeaders.add("Pragma", "public");
         * responseHeaders.add("Content-Transfer-Encoding","binary"); responseHeaders.add("content-disposition",
         * "attachment; filename=ForecastHours.xls");
         */
        // responseHeaders.add("Content-Type", "application/vnd.ms-excel");

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Download complete !!!", "")).build();
    }

    @RequestMapping(value = SHOW_FORECAST_FOR_ALL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public EmployeeHoursAdminDTO getForecastHoursForAllEmployees() {
        log.debug("REST request to get the Employee Hours for Coming Months for all employees");
        final List<Employee> employees = employeeService.findAll();
        final EmployeeHoursAdminDTO dto = new EmployeeHoursAdminDTO();
        dto.setAllEmployeeWithForecast(employeeHoursService.getForecastOfAllEmployee(employees));
        return dto;
    }

}
