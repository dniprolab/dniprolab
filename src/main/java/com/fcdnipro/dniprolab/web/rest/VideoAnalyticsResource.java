package com.fcdnipro.dniprolab.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fcdnipro.dniprolab.domain.User;
import com.fcdnipro.dniprolab.domain.Video;
import com.fcdnipro.dniprolab.domain.VideoAnalytics;
import com.fcdnipro.dniprolab.security.SecurityUtils;
import com.fcdnipro.dniprolab.service.UserService;
import com.fcdnipro.dniprolab.service.VideoAnalyticsService;
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
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing VideoAnalytics.
 */
@RestController
@RequestMapping("/api")
public class VideoAnalyticsResource {

    private final Logger log = LoggerFactory.getLogger(VideoAnalyticsResource.class);

    @Inject
    private VideoAnalyticsService videoAnalyticsService;

    @Inject
    private UserService userService;

    /**
     * POST  /videoAnalyticss -> Create a new videoAnalytics.
     */
    @RequestMapping(value = "/videoAnalyticss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VideoAnalytics> createVideoAnalytics(@Valid @RequestBody VideoAnalytics videoAnalytics) throws URISyntaxException {
        log.debug("REST request to save VideoAnalytics : {}", videoAnalytics);
        if (videoAnalytics.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("videoAnalytics", "idexists", "A new videoAnalytics cannot already have an ID")).body(null);
        }
        User user = userService.getCurrentUser();
        videoAnalytics.setAuthor(user.getFirstName() + " " + user.getLastName());
        videoAnalytics.setDate(LocalDate.now());
        VideoAnalytics result = videoAnalyticsService.save(videoAnalytics);
        return ResponseEntity.created(new URI("/api/videoAnalyticss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("videoAnalytics", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /videoAnalyticss -> Updates an existing videoAnalytics.
     */
    @RequestMapping(value = "/videoAnalyticss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VideoAnalytics> updateVideoAnalytics(@Valid @RequestBody VideoAnalytics videoAnalytics) throws URISyntaxException {
        log.debug("REST request to update VideoAnalytics : {}", videoAnalytics);
        if (videoAnalytics.getId() == null) {
            return createVideoAnalytics(videoAnalytics);
        }
        VideoAnalytics result = videoAnalyticsService.save(videoAnalytics);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("videoAnalytics", videoAnalytics.getId().toString()))
            .body(result);
    }

    /**
     * GET  /videoAnalyticss -> get all the videoAnalyticss.
     */
    @RequestMapping(value = "/videoAnalyticss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<VideoAnalytics>> getAllVideoAnalyticss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of VideoAnalyticss");
        Page<VideoAnalytics> page = videoAnalyticsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/videoAnalyticss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /videoAnalyticss/:id -> get the "id" videoAnalytics.
     */
    @RequestMapping(value = "/videoAnalyticss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VideoAnalytics> getVideoAnalytics(@PathVariable Long id) {
        log.debug("REST request to get VideoAnalytics : {}", id);
        VideoAnalytics videoAnalytics = videoAnalyticsService.findOne(id);
        return Optional.ofNullable(videoAnalytics)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /videoAnalyticss/:id -> delete the "id" videoAnalytics.
     */
    @RequestMapping(value = "/videoAnalyticss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVideoAnalytics(@PathVariable Long id) {
        log.debug("REST request to delete VideoAnalytics : {}", id);
        videoAnalyticsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("videoAnalytics", id.toString())).build();
    }

    /**
     * SEARCH  /_search/videoAnalyticss/:query -> search for the videoAnalytics corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/videoAnalyticss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<VideoAnalytics> searchVideoAnalyticss(@PathVariable String query) {
        log.debug("Request to search VideoAnalyticss for query {}", query);
        return videoAnalyticsService.search(query);
    }

    /*
    * GET all messages for current user
    * @return list of entities
     */
    @RequestMapping(value = "/user-videos", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<VideoAnalytics>> findAllVideosForCurrentUser(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get all videos for current user");
        Page<VideoAnalytics> result = videoAnalyticsService.findVideoForCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, "/api/video-analytics/users/");
        return new ResponseEntity<>(result.getContent(), headers, HttpStatus.OK);
    }
}
