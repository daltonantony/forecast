package com.opensolutions.forecast.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opensolutions.forecast.domain.EmployeeAllocation;
import com.opensolutions.forecast.service.EmployeeAllocationService;
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
 * REST controller for managing EmployeeAllocation.
 */
@RestController
@RequestMapping("/api")
public class EmployeeAllocationResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeAllocationResource.class);
        
    @Inject
    private EmployeeAllocationService employeeAllocationService;
    
    /**
     * POST  /employeeAllocations -> Create a new employeeAllocation.
     */
    @RequestMapping(value = "/employeeAllocations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeAllocation> createEmployeeAllocation(@RequestBody @Valid EmployeeAllocation employeeAllocation) throws URISyntaxException {
        log.debug("REST request to save EmployeeAllocation : {}", employeeAllocation);
        if (employeeAllocation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeAllocation", "idexists", "A new employeeAllocation cannot already have an ID")).body(null);
        }
        EmployeeAllocation result = employeeAllocationService.save(employeeAllocation);
        return ResponseEntity.created(new URI("/api/employeeAllocations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("Employee Allocation", result.getEmployee().getName()))
            .body(result);
    }

    /**
     * PUT  /employeeAllocations -> Updates an existing employeeAllocation.
     */
    @RequestMapping(value = "/employeeAllocations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeAllocation> updateEmployeeAllocation(@RequestBody @Valid EmployeeAllocation employeeAllocation) throws URISyntaxException {
        log.debug("REST request to update EmployeeAllocation : {}", employeeAllocation);
        if (employeeAllocation.getId() == null) {
            return createEmployeeAllocation(employeeAllocation);
        }
        EmployeeAllocation result = employeeAllocationService.save(employeeAllocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("Employee Allocation", employeeAllocation.getEmployee().getName()))
            .body(result);
    }

    /**
     * GET  /employeeAllocations -> get all the employeeAllocations.
     */
    @RequestMapping(value = "/employeeAllocations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeAllocation> getAllEmployeeAllocations() {
        log.debug("REST request to get all EmployeeAllocations");
        return employeeAllocationService.findAll();
            }

    /**
     * GET  /employeeAllocations/:id -> get the "id" employeeAllocation.
     */
    @RequestMapping(value = "/employeeAllocations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeAllocation> getEmployeeAllocation(@PathVariable Long id) {
        log.debug("REST request to get EmployeeAllocation : {}", id);
        EmployeeAllocation employeeAllocation = employeeAllocationService.findOne(id);
        return Optional.ofNullable(employeeAllocation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employeeAllocations/:id -> delete the "id" employeeAllocation.
     */
    @RequestMapping(value = "/employeeAllocations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployeeAllocation(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeAllocation : {}", id);
        employeeAllocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("Employee Allocation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/employeeAllocations/:query -> search for the employeeAllocation corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/employeeAllocations/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeAllocation> searchEmployeeAllocations(@PathVariable String query) {
        log.debug("Request to search EmployeeAllocations for query {}", query);
        return employeeAllocationService.search(query);
    }
}
