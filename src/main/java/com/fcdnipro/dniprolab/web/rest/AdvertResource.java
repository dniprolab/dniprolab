package com.fcdnipro.dniprolab.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fcdnipro.dniprolab.domain.Advert;
import com.fcdnipro.dniprolab.domain.User;
import com.fcdnipro.dniprolab.service.AdvertService;
import com.fcdnipro.dniprolab.service.UserService;
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
 * REST controller for managing Advert.
 */
@RestController
@RequestMapping("/api")
public class AdvertResource {

    private final Logger log = LoggerFactory.getLogger(AdvertResource.class);

    @Inject
    private AdvertService advertService;

    @Inject
    private UserService userService;

    /**
     * POST  /adverts -> Create a new advert.
     */
    @RequestMapping(value = "/adverts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Advert> createAdvert(@Valid @RequestBody Advert advert) throws URISyntaxException {
        log.debug("REST request to save Advert : {}", advert);
        if (advert.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("advert", "idexists", "A new advert cannot already have an ID")).body(null);
        }
        User user = userService.getCurrentUser();
        advert.setAuthor(user.getFirstName() + " " + user.getLastName());
        Advert result = advertService.save(advert);
        return ResponseEntity.created(new URI("/api/adverts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("advert", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adverts -> Updates an existing advert.
     */
    @RequestMapping(value = "/adverts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Advert> updateAdvert(@Valid @RequestBody Advert advert) throws URISyntaxException {
        log.debug("REST request to update Advert : {}", advert);
        if (advert.getId() == null) {
            return createAdvert(advert);
        }
        Advert result = advertService.save(advert);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("advert", advert.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adverts -> get all the adverts.
     */
    @RequestMapping(value = "/adverts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Advert>> getAllAdverts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Adverts");
        Page<Advert> page = advertService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/adverts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /adverts/:id -> get the "id" advert.
     */
    @RequestMapping(value = "/adverts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Advert> getAdvert(@PathVariable Long id) {
        log.debug("REST request to get Advert : {}", id);
        Advert advert = advertService.findOne(id);
        return Optional.ofNullable(advert)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /adverts/:id -> delete the "id" advert.
     */
    @RequestMapping(value = "/adverts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdvert(@PathVariable Long id) {
        log.debug("REST request to delete Advert : {}", id);
        advertService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("advert", id.toString())).build();
    }

    /**
     * SEARCH  /_search/adverts/:query -> search for the advert corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/adverts/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Advert> searchAdverts(@PathVariable String query) {
        log.debug("Request to search Adverts for query {}", query);
        return advertService.search(query);
    }
}
