package com.fcdnipro.dniprolab.web;

import com.codahale.metrics.annotation.Timed;
import com.fcdnipro.dniprolab.domain.Video;
import com.fcdnipro.dniprolab.repository.search.VideoSearchRepository;
import com.fcdnipro.dniprolab.security.SecurityUtils;
import com.fcdnipro.dniprolab.service.VideoService;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Rest controller for managing Video entities.
 */
@RestController
@RequestMapping("/api")
public class VideoController {

    private final  static Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Inject
    private VideoService videoService;

    @Inject
    private VideoSearchRepository videoSearchRepository;

    /**
     * POST method  /videos -> Create a new video entity.
     */
    @RequestMapping(value = "/videos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Video> createVideo(@Valid @RequestBody Video video) throws URISyntaxException {
        logger.debug("REST request save video entity : {}", video);
        if (video.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("video", "idexists", "A new video cannot already have an ID")).body(null);
        }
        Video result = videoService.save(video);
        return ResponseEntity.created(new URI("/api/videos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("video", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /video -> Updates an existing entity.
     */
    @RequestMapping(value = "/videos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Video> updateVideo(@Valid @RequestBody Video video) throws URISyntaxException {
        logger.debug("REST request to update method VideoController : {}", video);
        if (video.getId() == null) {
            return createVideo(video);
        }
        Video result = videoService.save(video);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("video", video.getId().toString()))
            .body(result);
    }

    /**
     * GET  /videos -> get all entities.
     */
    @RequestMapping(value = "/schedules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Video>> getAllVideos(Pageable pageable)
        throws URISyntaxException {
        logger.debug("REST request to get a list of video entities");
        Page<Video> list = videoService.getAllVideos(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(list, "/api/videos");
        return new ResponseEntity<>(list.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /videos/:id -> get the entity with particular id.
     */
    @RequestMapping(value = "/videos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Video> getVideo(@PathVariable Long id) {
        logger.debug("REST request to get video entity with id : {}", id);
        Video video = videoService.findById(id);
        return Optional.ofNullable(video)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /video/:id -> delete entity with particular id.
     */
    @RequestMapping(value = "/video/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        logger.debug("REST request to delete video : {}", id);
        videoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("video", id.toString())).build();
    }

    /**
     * SEARCH  /_search/schedules/:query -> search for entity which is corresponding to query
     */
    @RequestMapping(value = "/_search/videos/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Video> searchSchedules(@PathVariable String query) {
        logger.debug("Request to search videos for query {}", query);
        return videoService.search(query);
    }

    /*
    * GET all messages for current user
    * @return list of entities
     */
    @RequestMapping(value = "/videos/currentuser/", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Video>> findAllVideosForCurrentUser() throws URISyntaxException {
        logger.debug("REST request to get all videos for current user");
        String login = SecurityUtils.getCurrentUserLogin();
        List<Video> result = videoService.findVideoForCurrentUser(login);
        PageImpl<Video> list = new PageImpl<Video>(result);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(list, "/api/videos/currentuser/");
        return new ResponseEntity<>(list.getContent().stream()
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }
}
