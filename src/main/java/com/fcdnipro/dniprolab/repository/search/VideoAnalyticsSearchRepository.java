package com.fcdnipro.dniprolab.repository.search;

import com.fcdnipro.dniprolab.domain.VideoAnalytics;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the VideoAnalytics entity.
 */
public interface VideoAnalyticsSearchRepository extends ElasticsearchRepository<VideoAnalytics, Long> {
}
