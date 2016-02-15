package com.opensolutions.forecast.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opensolutions.forecast.domain.EmployeeBillingHours;
import com.opensolutions.forecast.service.EmployeeBillingHoursService;
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
 * REST controller for managing EmployeeBillingHours.
 */
@RestController
@RequestMapping("/api")
public class EmployeeBillingHoursResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeBillingHoursResource.class);
        
    @Inject
    private EmployeeBillingHoursService employeeBillingHoursService;
    
    /**
     * POST  /employeeBillingHourss -> Create a new employeeBillingHours.
     */
    @RequestMapping(value = "/employeeBillingHourss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeBillingHours> createEmployeeBillingHours(@RequestBody @Valid EmployeeBillingHours employeeBillingHours) throws URISyntaxException {
        log.debug("REST request to save EmployeeBillingHours : {}", employeeBillingHours);
        if (employeeBillingHours.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeBillingHours", "idexists", "A new employeeBillingHours cannot already have an ID")).body(null);
        }
        EmployeeBillingHours result = employeeBillingHoursService.save(employeeBillingHours);
        return ResponseEntity.created(new URI("/api/employeeBillingHourss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("EmployeeBillingHours", result.getEmployee().getName()))
            .body(result);
    }

    /**
     * PUT  /employeeBillingHourss -> Updates an existing employeeBillingHours.
     */
    @RequestMapping(value = "/employeeBillingHourss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeBillingHours> updateEmployeeBillingHours(@RequestBody @Valid EmployeeBillingHours employeeBillingHours) throws URISyntaxException {
        log.debug("REST request to update EmployeeBillingHours : {}", employeeBillingHours);
        if (employeeBillingHours.getId() == null) {
            return createEmployeeBillingHours(employeeBillingHours);
        }
        EmployeeBillingHours result = employeeBillingHoursService.save(employeeBillingHours);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("EmployeeBillingHours", employeeBillingHours.getEmployee().getName()))
            .body(result);
    }

    /**
     * GET  /employeeBillingHourss -> get all the employeeBillingHourss.
     */
    @RequestMapping(value = "/employeeBillingHourss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeBillingHours> getAllEmployeeBillingHourss() {
        log.debug("REST request to get all EmployeeBillingHourss");
        return employeeBillingHoursService.findAll();
            }

    /**
     * GET  /employeeBillingHourss/:id -> get the "id" employeeBillingHours.
     */
    @RequestMapping(value = "/employeeBillingHourss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeBillingHours> getEmployeeBillingHours(@PathVariable Long id) {
        log.debug("REST request to get EmployeeBillingHours : {}", id);
        EmployeeBillingHours employeeBillingHours = employeeBillingHoursService.findOne(id);
        return Optional.ofNullable(employeeBillingHours)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employeeBillingHourss/:id -> delete the "id" employeeBillingHours.
     */
    @RequestMapping(value = "/employeeBillingHourss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployeeBillingHours(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeBillingHours : {}", id);
        employeeBillingHoursService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("EmployeeBillingHours", id.toString())).build();
    }

    /**
     * SEARCH  /_search/employeeBillingHourss/:query -> search for the employeeBillingHours corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/employeeBillingHourss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeBillingHours> searchEmployeeBillingHourss(@PathVariable String query) {
        log.debug("Request to search EmployeeBillingHourss for query {}", query);
        return employeeBillingHoursService.search(query);
    }
}
