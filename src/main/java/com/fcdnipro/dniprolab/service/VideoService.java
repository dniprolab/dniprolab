package com.fcdnipro.dniprolab.service;

import com.codahale.metrics.annotation.Timed;
import com.fcdnipro.dniprolab.domain.Schedule;
import com.fcdnipro.dniprolab.domain.Video;
import com.fcdnipro.dniprolab.repository.VideoRepository;
import com.fcdnipro.dniprolab.repository.search.VideoSearchRepository;
import com.fcdnipro.dniprolab.web.rest.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service class to manage video entity
 */

@Service
@Transactional
public class VideoService {

    private final static Logger logger = LoggerFactory.getLogger(VideoService.class);

    @Inject
    private VideoRepository videoRepository;

    @Inject
    private VideoSearchRepository videoSearchRepository;

    /*
    *Saving persisted video entity method.
    *@return  persisted entity.
     */
    public Video save(Video video){
        logger.debug("Invoke save method eor entity {}", video);
        Video saved = videoRepository.saveAndFlush(video);
        videoSearchRepository.save(saved);
        return saved;
    }

    /*
    * Get all method.
    * @return List of all existing video entities.
     */
    @Transactional(readOnly = true)
    public Page<Video> getAllVideos(Pageable pageable){
        logger.debug("Invoke get all videos method");
        Page<Video> result = videoRepository.findAll(pageable);
        return result;
    }

    /*
    *Find video entity by id
    * @return video entity
     */
    @Transactional(readOnly = true)
    public Video findById(Long id){
        logger.debug("Invoke find by id method with id ", id);
        return videoRepository.findOne(id);
    }

    /*
    * Delete video entity by id
     */
    public void delete(Long id){
        logger.debug("Invoke delete method with id ", id);
        videoRepository.delete(id);
        videoSearchRepository.delete(id);
    }

    /*
    * Find videos for user by user login
    * @return List of video entities
     */
    public List<Video> findVideoForCurrentUser(String login){
        logger.debug("Invoke find all videos for current user method with login ", login);
        List<Video> videos = videoRepository.findAllVideosForCurrentUser(login);
        return videos;
    }

    /*
    * Search method
    * @param string with query
    * @return List of entities
     */
    @Transactional(readOnly = true)
    public List<Video> search(String query) {

        logger.debug("REST request to search videos with query {}", query);
        return StreamSupport
            .stream(videoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
