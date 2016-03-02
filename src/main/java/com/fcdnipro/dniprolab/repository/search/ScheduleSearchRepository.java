package com.fcdnipro.dniprolab.repository.search;

import com.fcdnipro.dniprolab.domain.Schedule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Schedule entity.
 */
public interface ScheduleSearchRepository extends ElasticsearchRepository<Schedule, Long> {
}
