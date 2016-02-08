package com.opensolutions.forecast.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opensolutions.forecast.domain.ROLE_TARIFF;
import com.opensolutions.forecast.service.ROLE_TARIFFService;
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
 * REST controller for managing ROLE_TARIFF.
 */
@RestController
@RequestMapping("/api")
public class ROLE_TARIFFResource {

    private final Logger log = LoggerFactory.getLogger(ROLE_TARIFFResource.class);
        
    @Inject
    private ROLE_TARIFFService rOLE_TARIFFService;
    
    /**
     * POST  /rOLE_TARIFFs -> Create a new rOLE_TARIFF.
     */
    @RequestMapping(value = "/rOLE_TARIFFs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ROLE_TARIFF> createROLE_TARIFF(@RequestBody @Valid ROLE_TARIFF rOLE_TARIFF) throws URISyntaxException {
        log.debug("REST request to save ROLE_TARIFF : {}", rOLE_TARIFF);
        if (rOLE_TARIFF.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rOLE_TARIFF", "idexists", "A new rOLE_TARIFF cannot already have an ID")).body(null);
        }
        ROLE_TARIFF result = rOLE_TARIFFService.save(rOLE_TARIFF);
        return ResponseEntity.created(new URI("/api/rOLE_TARIFFs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("Role Tariff", result.getRole()))
            .body(result);
    }

    /**
     * PUT  /rOLE_TARIFFs -> Updates an existing rOLE_TARIFF.
     */
    @RequestMapping(value = "/rOLE_TARIFFs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ROLE_TARIFF> updateROLE_TARIFF(@RequestBody @Valid ROLE_TARIFF rOLE_TARIFF) throws URISyntaxException {
        log.debug("REST request to update ROLE_TARIFF : {}", rOLE_TARIFF);
        if (rOLE_TARIFF.getId() == null) {
            return createROLE_TARIFF(rOLE_TARIFF);
        }
        ROLE_TARIFF result = rOLE_TARIFFService.save(rOLE_TARIFF);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("Role Tariff", rOLE_TARIFF.getRole()))
            .body(result);
    }

    /**
     * GET  /rOLE_TARIFFs -> get all the rOLE_TARIFFs.
     */
    @RequestMapping(value = "/rOLE_TARIFFs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ROLE_TARIFF> getAllROLE_TARIFFs() {
        log.debug("REST request to get all ROLE_TARIFFs");
        return rOLE_TARIFFService.findAll();
            }

    /**
     * GET  /rOLE_TARIFFs/:id -> get the "id" rOLE_TARIFF.
     */
    @RequestMapping(value = "/rOLE_TARIFFs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ROLE_TARIFF> getROLE_TARIFF(@PathVariable Long id) {
        log.debug("REST request to get ROLE_TARIFF : {}", id);
        ROLE_TARIFF rOLE_TARIFF = rOLE_TARIFFService.findOne(id);
        return Optional.ofNullable(rOLE_TARIFF)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rOLE_TARIFFs/:id -> delete the "id" rOLE_TARIFF.
     */
    @RequestMapping(value = "/rOLE_TARIFFs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteROLE_TARIFF(@PathVariable Long id) {
        log.debug("REST request to delete ROLE_TARIFF : {}", id);
        rOLE_TARIFFService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("Role Tariff", id.toString())).build();
    }

    /**
     * SEARCH  /_search/rOLE_TARIFFs/:query -> search for the rOLE_TARIFF corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/rOLE_TARIFFs/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ROLE_TARIFF> searchROLE_TARIFFs(@PathVariable String query) {
        log.debug("Request to search ROLE_TARIFFs for query {}", query);
        return rOLE_TARIFFService.search(query);
    }
}
