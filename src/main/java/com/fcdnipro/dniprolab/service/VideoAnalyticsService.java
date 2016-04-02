package com.fcdnipro.dniprolab.service;

import com.fcdnipro.dniprolab.domain.VideoAnalytics;
import com.fcdnipro.dniprolab.repository.VideoAnalyticsRepository;
import com.fcdnipro.dniprolab.repository.search.VideoAnalyticsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing VideoAnalytics.
 */
@Service
@Transactional
public class VideoAnalyticsService {

    private final Logger log = LoggerFactory.getLogger(VideoAnalyticsService.class);

    @Inject
    private VideoAnalyticsRepository videoAnalyticsRepository;

    @Inject
    private UserService userService;

    @Inject
    private VideoAnalyticsSearchRepository videoAnalyticsSearchRepository;

    /**
     * Save a videoAnalytics.
     * @return the persisted entity
     */
    public VideoAnalytics save(VideoAnalytics videoAnalytics) {
        log.debug("Request to save VideoAnalytics : {}", videoAnalytics);
        VideoAnalytics result = videoAnalyticsRepository.save(videoAnalytics);
        videoAnalyticsSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the videoAnalyticss.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VideoAnalytics> findAll(Pageable pageable) {
        log.debug("Request to get all VideoAnalyticss");
        Page<VideoAnalytics> result = videoAnalyticsRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one videoAnalytics by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public VideoAnalytics findOne(Long id) {
        log.debug("Request to get VideoAnalytics : {}", id);
        VideoAnalytics videoAnalytics = videoAnalyticsRepository.findOne(id);
        return videoAnalytics;
    }

    /**
     *  delete the  videoAnalytics by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete VideoAnalytics : {}", id);
        videoAnalyticsRepository.delete(id);
        videoAnalyticsSearchRepository.delete(id);
    }

    /**
     * search for the videoAnalytics corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<VideoAnalytics> search(String query) {

        log.debug("REST request to search VideoAnalyticss for query {}", query);
        return StreamSupport
            .stream(videoAnalyticsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /*
    * Get all videoAnalytic entities where recipient is current logged user
    * @return List of entities
     */
    public Page<VideoAnalytics> findVideoForCurrentUser(Pageable pageable){
        String login = userService.getCurrentUser().getLogin();
        log.debug("Invoke find all videos for current user method with login ", login);
        Page<VideoAnalytics> videos = videoAnalyticsRepository.findAllVideosForCurrentUser(login, pageable);
        return videos;
    }
}
