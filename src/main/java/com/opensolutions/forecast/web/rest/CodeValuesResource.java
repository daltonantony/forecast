package com.opensolutions.forecast.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opensolutions.forecast.domain.CodeValues;
import com.opensolutions.forecast.service.CodeValuesService;
import com.opensolutions.forecast.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CodeValues.
 */
@RestController
@RequestMapping("/api")
public class CodeValuesResource {

    private final Logger log = LoggerFactory.getLogger(CodeValuesResource.class);
        
    @Inject
    private CodeValuesService codeValuesService;
    
    /**
     * POST  /codeValuess -> Create a new codeValues.
     */
    @RequestMapping(value = "/codeValuess",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CodeValues> createCodeValues(@RequestBody CodeValues codeValues) throws URISyntaxException {
        log.debug("REST request to save CodeValues : {}", codeValues);
        if (codeValues.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("codeValues", "idexists", "A new codeValues cannot already have an ID")).body(null);
        }
        CodeValues result = codeValuesService.save(codeValues);
        return ResponseEntity.created(new URI("/api/codeValuess/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("codeValues", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /codeValuess -> Updates an existing codeValues.
     */
    @RequestMapping(value = "/codeValuess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CodeValues> updateCodeValues(@RequestBody CodeValues codeValues) throws URISyntaxException {
        log.debug("REST request to update CodeValues : {}", codeValues);
        if (codeValues.getId() == null) {
            return createCodeValues(codeValues);
        }
        CodeValues result = codeValuesService.save(codeValues);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("codeValues", codeValues.getId().toString()))
            .body(result);
    }

    /**
     * GET  /codeValuess -> get all the codeValuess.
     */
    @RequestMapping(value = "/codeValuess",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CodeValues> getAllCodeValuess() {
        log.debug("REST request to get all CodeValuess");
        return codeValuesService.findAll();
            }

    /**
     * GET  /codeValuess/:id -> get the "id" codeValues.
     */
    @RequestMapping(value = "/codeValuess/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CodeValues> getCodeValues(@PathVariable Long id) {
        log.debug("REST request to get CodeValues : {}", id);
        CodeValues codeValues = codeValuesService.findOne(id);
        return Optional.ofNullable(codeValues)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /codeValuess/:id -> delete the "id" codeValues.
     */
    @RequestMapping(value = "/codeValuess/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCodeValues(@PathVariable Long id) {
        log.debug("REST request to delete CodeValues : {}", id);
        codeValuesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("codeValues", id.toString())).build();
    }

    /**
     * SEARCH  /_search/codeValuess/:query -> search for the codeValues corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/codeValuess/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CodeValues> searchCodeValuess(@PathVariable String query) {
        log.debug("Request to search CodeValuess for query {}", query);
        return codeValuesService.search(query);
    }
}
