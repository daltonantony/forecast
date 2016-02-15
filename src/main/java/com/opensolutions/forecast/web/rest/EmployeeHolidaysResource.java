package com.opensolutions.forecast.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opensolutions.forecast.domain.EmployeeHolidays;
import com.opensolutions.forecast.service.EmployeeHolidaysService;
import com.opensolutions.forecast.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing EmployeeHolidays.
 */
@RestController
@RequestMapping("/api")
public class EmployeeHolidaysResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeHolidaysResource.class);
        
    @Inject
    private EmployeeHolidaysService employeeHolidaysService;
    
    /**
     * POST  /employeeHolidayss -> Create a new employeeHolidays.
     */
    @RequestMapping(value = "/employeeHolidayss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeHolidays> createEmployeeHolidays(@RequestBody @Valid EmployeeHolidays employeeHolidays) throws URISyntaxException {
        log.debug("REST request to save EmployeeHolidays : {}", employeeHolidays);
        if (employeeHolidays.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeHolidays", "idexists", "A new employeeHolidays cannot already have an ID")).body(null);
        }
        EmployeeHolidays result = employeeHolidaysService.save(employeeHolidays);
        return ResponseEntity.created(new URI("/api/employeeHolidayss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("EmployeeHolidays", result.getEmployeeBillingHours().getEmployee().getName()))
            .body(result);
    }

    /**
     * PUT  /employeeHolidayss -> Updates an existing employeeHolidays.
     */
    @RequestMapping(value = "/employeeHolidayss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeHolidays> updateEmployeeHolidays(@RequestBody @Valid EmployeeHolidays employeeHolidays) throws URISyntaxException {
        log.debug("REST request to update EmployeeHolidays : {}", employeeHolidays);
        if (employeeHolidays.getId() == null) {
            return createEmployeeHolidays(employeeHolidays);
        }
        EmployeeHolidays result = employeeHolidaysService.save(employeeHolidays);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("EmployeeHolidays", employeeHolidays.getEmployeeBillingHours().getEmployee().getName()))
            .body(result);
    }

    /**
     * GET  /employeeHolidayss -> get all the employeeHolidayss.
     */
    @RequestMapping(value = "/employeeHolidayss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeHolidays> getAllEmployeeHolidayss() {
        log.debug("REST request to get all EmployeeHolidayss");
        return employeeHolidaysService.findAll();
            }

    /**
     * GET  /employeeHolidayss/:id -> get the "id" employeeHolidays.
     */
    @RequestMapping(value = "/employeeHolidayss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeHolidays> getEmployeeHolidays(@PathVariable Long id) {
        log.debug("REST request to get EmployeeHolidays : {}", id);
        EmployeeHolidays employeeHolidays = employeeHolidaysService.findOne(id);
        return Optional.ofNullable(employeeHolidays)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employeeHolidayss/:id -> delete the "id" employeeHolidays.
     */
    @RequestMapping(value = "/employeeHolidayss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployeeHolidays(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeHolidays : {}", id);
        employeeHolidaysService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("EmployeeHolidays", id.toString())).build();
    }

    /**
     * SEARCH  /_search/employeeHolidayss/:query -> search for the employeeHolidays corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/employeeHolidayss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeHolidays> searchEmployeeHolidayss(@PathVariable String query) {
        log.debug("Request to search EmployeeHolidayss for query {}", query);
        return employeeHolidaysService.search(query);
    }
}
