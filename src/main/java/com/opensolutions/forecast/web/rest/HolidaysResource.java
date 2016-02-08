package com.opensolutions.forecast.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opensolutions.forecast.domain.Holidays;
import com.opensolutions.forecast.service.HolidaysService;
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
 * REST controller for managing Holidays.
 */
@RestController
@RequestMapping("/api")
public class HolidaysResource {

    private final Logger log = LoggerFactory.getLogger(HolidaysResource.class);
        
    @Inject
    private HolidaysService holidaysService;
    
    /**
     * POST  /holidayss -> Create a new holidays.
     */
    @RequestMapping(value = "/holidayss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Holidays> createHolidays(@RequestBody @Valid Holidays holidays) throws URISyntaxException {
        log.debug("REST request to save Holidays : {}", holidays);
        if (holidays.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("holidays", "idexists", "A new holidays cannot already have an ID")).body(null);
        }
        Holidays result = holidaysService.save(holidays);
        return ResponseEntity.created(new URI("/api/holidayss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("Holiday", result.getName()))
            .body(result);
    }

    /**
     * PUT  /holidayss -> Updates an existing holidays.
     */
    @RequestMapping(value = "/holidayss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Holidays> updateHolidays(@RequestBody @Valid Holidays holidays) throws URISyntaxException {
        log.debug("REST request to update Holidays : {}", holidays);
        if (holidays.getId() == null) {
            return createHolidays(holidays);
        }
        Holidays result = holidaysService.save(holidays);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("Holiday", holidays.getName()))
            .body(result);
    }

    /**
     * GET  /holidayss -> get all the holidayss.
     */
    @RequestMapping(value = "/holidayss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Holidays> getAllHolidayss() {
        log.debug("REST request to get all Holidayss");
        return holidaysService.findAll();
            }

    /**
     * GET  /holidayss/:id -> get the "id" holidays.
     */
    @RequestMapping(value = "/holidayss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Holidays> getHolidays(@PathVariable Long id) {
        log.debug("REST request to get Holidays : {}", id);
        Holidays holidays = holidaysService.findOne(id);
        return Optional.ofNullable(holidays)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /holidayss/:id -> delete the "id" holidays.
     */
    @RequestMapping(value = "/holidayss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHolidays(@PathVariable Long id) {
        log.debug("REST request to delete Holidays : {}", id);
        holidaysService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("Holiday", id.toString())).build();
    }

    /**
     * SEARCH  /_search/holidayss/:query -> search for the holidays corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/holidayss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Holidays> searchHolidayss(@PathVariable String query) {
        log.debug("Request to search Holidayss for query {}", query);
        return holidaysService.search(query);
    }
}
