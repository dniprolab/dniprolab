package com.fcdnipro.dniprolab.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fcdnipro.dniprolab.domain.Advertisement;
import com.fcdnipro.dniprolab.service.AdvertisementService;
import com.fcdnipro.dniprolab.smsNotification.NotificationType;
import com.fcdnipro.dniprolab.smsNotification.SmsNotificationService;
import com.fcdnipro.dniprolab.web.rest.util.HeaderUtil;
import com.fcdnipro.dniprolab.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
 * REST controller for managing Advertisement.
 */
@RestController
@RequestMapping("/api")
public class AdvertisementResource {

    private final Logger log = LoggerFactory.getLogger(AdvertisementResource.class);

    private final static NotificationType notificationType = NotificationType.ADVERTISEMENT;

    @Inject
    SmsNotificationService smsNotificationService;

    @Inject
    private AdvertisementService advertisementService;

    /**
     * POST  /advertisements -> Create a new advertisement.
     */
    @RequestMapping(value = "/advertisements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Advertisement> createAdvertisement(@Valid @RequestBody Advertisement advertisement) throws URISyntaxException {
        log.debug("REST request to save Advertisement : {}", advertisement);
        if (advertisement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("advertisement", "idexists", "A new advertisement cannot already have an ID")).body(null);
        }
        Advertisement result = advertisementService.save(advertisement);

        log.info(smsNotificationService.notifyUser(notificationType));

        return ResponseEntity.created(new URI("/api/advertisements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("advertisement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /advertisements -> Updates an existing advertisement.
     */
    @RequestMapping(value = "/advertisements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Advertisement> updateAdvertisement(@Valid @RequestBody Advertisement advertisement) throws URISyntaxException {
        log.debug("REST request to update Advertisement : {}", advertisement);
        if (advertisement.getId() == null) {
            return createAdvertisement(advertisement);
        }
        Advertisement result = advertisementService.save(advertisement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("advertisement", advertisement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /advertisements -> get all the advertisements.
     */
    @RequestMapping(value = "/advertisements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Advertisement>> getAllAdvertisements(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Advertisements");
        Page<Advertisement> page = advertisementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/advertisements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /advertisements/:id -> get the "id" advertisement.
     */
    @RequestMapping(value = "/advertisements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Advertisement> getAdvertisement(@PathVariable Long id) {
        log.debug("REST request to get Advertisement : {}", id);
        Advertisement advertisement = advertisementService.findOne(id);
        return Optional.ofNullable(advertisement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /advertisements/:id -> delete the "id" advertisement.
     */
    @RequestMapping(value = "/advertisements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id) {
        log.debug("REST request to delete Advertisement : {}", id);
        advertisementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("advertisement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/advertisements/:query -> search for the advertisement corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/advertisements/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Advertisement> searchAdvertisements(@PathVariable String query) {
        log.debug("Request to search Advertisements for query {}", query);
        return advertisementService.search(query);
    }
}
