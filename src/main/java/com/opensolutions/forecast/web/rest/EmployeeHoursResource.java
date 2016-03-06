package com.opensolutions.forecast.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.opensolutions.forecast.domain.Employee;
import com.opensolutions.forecast.domain.EmployeeHours;
import com.opensolutions.forecast.service.EmployeeHoursService;
import com.opensolutions.forecast.web.rest.dto.EmployeeHoursDTO;
import com.opensolutions.forecast.web.rest.util.HeaderUtil;

/**
 * REST controller for managing EmployeeHours.
 */
@RestController
@RequestMapping("/api")
public class EmployeeHoursResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeHoursResource.class);

    @Inject
    private EmployeeHoursService employeeHoursService;

    /**
     * POST  /employeeHourss -> Create a new employeeHours.
     */
    @RequestMapping(value = "/employeeHourss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeHours> createEmployeeHours(@RequestBody final EmployeeHours employeeHours) throws URISyntaxException {
        log.debug("REST request to save EmployeeHours : {}", employeeHours);
        if (employeeHours.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeHours", "idexists", "A new employeeHours cannot already have an ID")).body(null);
        }
        final EmployeeHours result = employeeHoursService.save(employeeHours);
        return ResponseEntity.created(new URI("/api/employeeHourss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employeeHours", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employeeHourss -> Updates an existing employeeHours.
     */
    @RequestMapping(value = "/employeeHourss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeHours> updateEmployeeHours(@RequestBody final EmployeeHours employeeHours) throws URISyntaxException {
        log.debug("REST request to update EmployeeHours : {}", employeeHours);
        if (employeeHours.getId() == null) {
            return createEmployeeHours(employeeHours);
        }
        final EmployeeHours result = employeeHoursService.save(employeeHours);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employeeHours", employeeHours.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employeeHourss -> get all the employeeHourss.
     */
    @RequestMapping(value = "/employeeHourss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeHours> getAllEmployeeHourss() {
        log.debug("REST request to get all EmployeeHourss");
        return employeeHoursService.findAll();
            }

    /**
     * GET  /employeeHourss/:id -> get the "id" employeeHours.
     */
    @RequestMapping(value = "/employeeHourss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeHours> getEmployeeHours(@PathVariable final Long id) {
        log.debug("REST request to get EmployeeHours : {}", id);
        final EmployeeHours employeeHours = employeeHoursService.findOne(id);
        return Optional.ofNullable(employeeHours)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employeeHourss/:id -> delete the "id" employeeHours.
     */
    @RequestMapping(value = "/employeeHourss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployeeHours(@PathVariable final Long id) {
        log.debug("REST request to delete EmployeeHours : {}", id);
        employeeHoursService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employeeHours", id.toString())).build();
    }

    /**
     * SEARCH  /_search/employeeHourss/:query -> search for the employeeHours corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/employeeHourss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeHours> searchEmployeeHourss(@PathVariable final String query) {
        log.debug("Request to search EmployeeHourss for query {}", query);
        return employeeHoursService.search(query);
    }

    @RequestMapping(value = "/employeeHoursForComingMonths",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public EmployeeHoursDTO getComingMonthsHours() {
        log.debug("REST request to get the Employee Hours for Coming Months");
        final EmployeeHoursDTO dto = new EmployeeHoursDTO();
        dto.setEmployeeHoursForComingMonths(employeeHoursService.getEmployeeHoursForComingMonths());
        dto.setForecastFreezePeriod(employeeHoursService.isForecastFreezePeriod());
		return dto;
    }

    @RequestMapping(value = "/employeeHoursForComingMonths",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> saveComingMonthsHours(@RequestBody final EmployeeHoursDTO dto) {
        log.debug("REST request to save the Employee Hours for Coming Months");
        final Employee employee = employeeHoursService.saveEmployeeHoursForComingMonths(dto.getEmployeeHoursForComingMonths());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert("Employee Hours", employee.getAssociateId().toString())).build();
    }

    @RequestMapping(value = "/employeeHoursForPreviousMonths",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Map<LocalDate, List<EmployeeHours>> getPreviousMonthsHours() {
        log.debug("REST request to get the Employee Hours for Previous Months");
        return employeeHoursService.getEmployeeHoursForPreviousMonths();
    }

}
