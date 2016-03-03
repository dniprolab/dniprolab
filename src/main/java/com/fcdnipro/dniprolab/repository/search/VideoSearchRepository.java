package com.fcdnipro.dniprolab.repository.search;

import com.fcdnipro.dniprolab.domain.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elastic Search Repository for video entity.
 */
public interface VideoSearchRepository extends ElasticsearchRepository<Video, Long> {
}
