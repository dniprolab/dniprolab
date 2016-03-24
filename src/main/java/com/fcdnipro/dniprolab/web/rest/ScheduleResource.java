package com.fcdnipro.dniprolab.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fcdnipro.dniprolab.domain.Schedule;
import com.fcdnipro.dniprolab.service.ScheduleService;
import com.fcdnipro.dniprolab.web.rest.util.HeaderUtil;
import com.fcdnipro.dniprolab.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Schedule.
 */
@RestController
@RequestMapping("/api")
public class ScheduleResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleResource.class);

    @Inject
    private ScheduleService scheduleService;

    /**
     * POST  /schedules -> Create a new schedule.
     */
    @RequestMapping(value = "/schedules",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody Schedule schedule) throws URISyntaxException {
        log.debug("REST request to save Schedule : {}", schedule);
        if (schedule.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("schedule", "idexists", "A new schedule cannot already have an ID")).body(null);
        }
        Schedule result = scheduleService.save(schedule);
        return ResponseEntity.created(new URI("/api/schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("schedule", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schedules -> Updates an existing schedule.
     */
    @RequestMapping(value = "/schedules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Schedule> updateSchedule(@Valid @RequestBody Schedule schedule) throws URISyntaxException {
        log.debug("REST request to update Schedule : {}", schedule);
        if (schedule.getId() == null) {
            return createSchedule(schedule);
        }
        Schedule result = scheduleService.save(schedule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("schedule", schedule.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schedules -> get all the schedules.
     */
    @RequestMapping(value = "/schedules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Schedule>> getAllSchedules(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Schedules");
        Page<Schedule> page = scheduleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schedules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /schedules/:id -> get the "id" schedule.
     */
    @RequestMapping(value = "/schedules/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Schedule> getSchedule(@PathVariable Long id) {
        log.debug("REST request to get Schedule : {}", id);
        Schedule schedule = scheduleService.findOne(id);
        return Optional.ofNullable(schedule)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /schedules/:id -> delete the "id" schedule.
     */
    @RequestMapping(value = "/schedules/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        log.debug("REST request to delete Schedule : {}", id);
        scheduleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("schedule", id.toString())).build();
    }

    /**
     * SEARCH  /_search/schedules/:query -> search for the schedule corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/schedules/{query:.+}",
                            method = RequestMethod.GET,
                            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Schedule> searchSchedules(@PathVariable String query) {
        log.debug("Request to search Schedules for query {}", query);
        return scheduleService.search(query);
    }

    /*
    * GET /schedules/widget should return 5 closest to current date schedule entries
     */
    @RequestMapping(value = "schedules/widget",
                                method = RequestMethod.GET,
                                produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Schedule>> getFiveClosestScheduleEntries() throws URISyntaxException {
        log.debug("Invoke REST request to find 5 closest schedule entries");
        List<Schedule> schedules = scheduleService.getFiveClosestEntries();
        PageImpl<Schedule> page = new PageImpl<Schedule>(schedules);
        HttpHeaders httpHeaders = PaginationUtil.generatePaginationHttpHeaders(page, "api/schedules/widget");
        return new ResponseEntity<List<Schedule>>(page.getContent(), httpHeaders, HttpStatus.OK);
    }
}
