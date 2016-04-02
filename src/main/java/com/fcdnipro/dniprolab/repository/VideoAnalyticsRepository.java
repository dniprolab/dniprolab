package com.fcdnipro.dniprolab.repository;

import com.fcdnipro.dniprolab.domain.Video;
import com.fcdnipro.dniprolab.domain.VideoAnalytics;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the VideoAnalytics entity.
 */
public interface VideoAnalyticsRepository extends JpaRepository<VideoAnalytics,Long> {

    @Query("select videoAnalytics from VideoAnalytics videoAnalytics where videoAnalytics.user.login = ?#{principal.username}")
    List<VideoAnalytics> findByUserIsCurrentUser();

    @Query("select videoAnalytics from VideoAnalytics videoAnalytics where videoAnalytics.user.login = ?1")
    Page<VideoAnalytics> findAllVideosForCurrentUser(String login, Pageable pageable);

}
