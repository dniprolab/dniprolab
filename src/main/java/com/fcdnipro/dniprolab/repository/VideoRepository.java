package com.fcdnipro.dniprolab.repository;

import com.fcdnipro.dniprolab.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
* Spring Data JPA repository for Video entity.
 */
public interface VideoRepository extends JpaRepository<Video,Long> {

    @Query("select video from Video video where video.user.login = ?1")
    public List<Video> findAllVideosForCurrentUser(String login);

}
