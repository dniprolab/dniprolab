package com.fcdnipro.dniprolab.service;

import com.fcdnipro.dniprolab.domain.Advertisement;
import com.fcdnipro.dniprolab.repository.AdvertisementRepository;
import com.fcdnipro.dniprolab.repository.search.AdvertisementSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Advertisement.
 */
@Service
@Transactional
public class AdvertisementService {

    private final Logger log = LoggerFactory.getLogger(AdvertisementService.class);
    
    @Inject
    private AdvertisementRepository advertisementRepository;
    
    @Inject
    private AdvertisementSearchRepository advertisementSearchRepository;
    
    /**
     * Save a advertisement.
     * @return the persisted entity
     */
    public Advertisement save(Advertisement advertisement) {
        log.debug("Request to save Advertisement : {}", advertisement);
        Advertisement result = advertisementRepository.save(advertisement);
        advertisementSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the advertisements.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Advertisement> findAll(Pageable pageable) {
        log.debug("Request to get all Advertisements");
        Page<Advertisement> result = advertisementRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one advertisement by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Advertisement findOne(Long id) {
        log.debug("Request to get Advertisement : {}", id);
        Advertisement advertisement = advertisementRepository.findOne(id);
        return advertisement;
    }

    /**
     *  delete the  advertisement by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Advertisement : {}", id);
        advertisementRepository.delete(id);
        advertisementSearchRepository.delete(id);
    }

    /**
     * search for the advertisement corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Advertisement> search(String query) {
        
        log.debug("REST request to search Advertisements for query {}", query);
        return StreamSupport
            .stream(advertisementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
