package com.fcdnipro.dniprolab.repository.search;

import com.fcdnipro.dniprolab.domain.Advert;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Advert entity.
 */
public interface AdvertSearchRepository extends ElasticsearchRepository<Advert, Long> {
}
