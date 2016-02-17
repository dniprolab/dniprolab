package com.fcdnipro.dniprolab.repository;

import com.fcdnipro.dniprolab.domain.Message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Message entity.
 */
public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("select message from Message message where message.user.login = ?#{principal.username}")
    List<Message> findByUserIsCurrentUser();

    @Query("select message from Message message where message.user.login = ?1")
    List<Message> findAllMessagesForCurrentUser(String login);
}
