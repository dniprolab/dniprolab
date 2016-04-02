package com.fcdnipro.dniprolab.service;

import com.fcdnipro.dniprolab.domain.Advert;
import com.fcdnipro.dniprolab.repository.AdvertRepository;
import com.fcdnipro.dniprolab.repository.search.AdvertSearchRepository;
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
 * Service Implementation for managing Advert.
 */
@Service
@Transactional
public class AdvertService {

    private final Logger log = LoggerFactory.getLogger(AdvertService.class);
    
    @Inject
    private AdvertRepository advertRepository;
    
    @Inject
    private AdvertSearchRepository advertSearchRepository;
    
    /**
     * Save a advert.
     * @return the persisted entity
     */
    public Advert save(Advert advert) {
        log.debug("Request to save Advert : {}", advert);
        Advert result = advertRepository.save(advert);
        advertSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the adverts.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Advert> findAll(Pageable pageable) {
        log.debug("Request to get all Adverts");
        Page<Advert> result = advertRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one advert by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Advert findOne(Long id) {
        log.debug("Request to get Advert : {}", id);
        Advert advert = advertRepository.findOne(id);
        return advert;
    }

    /**
     *  delete the  advert by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Advert : {}", id);
        advertRepository.delete(id);
        advertSearchRepository.delete(id);
    }

    /**
     * search for the advert corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Advert> search(String query) {
        
        log.debug("REST request to search Adverts for query {}", query);
        return StreamSupport
            .stream(advertSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
