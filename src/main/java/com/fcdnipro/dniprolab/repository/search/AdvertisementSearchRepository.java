package com.fcdnipro.dniprolab.repository.search;

import com.fcdnipro.dniprolab.domain.Advertisement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Advertisement entity.
 */
public interface AdvertisementSearchRepository extends ElasticsearchRepository<Advertisement, Long> {
}
