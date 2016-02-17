package com.fcdnipro.dniprolab.service;

import com.fcdnipro.dniprolab.domain.Message;
import com.fcdnipro.dniprolab.repository.MessageRepository;
import com.fcdnipro.dniprolab.repository.search.MessageSearchRepository;
import com.fcdnipro.dniprolab.security.SecurityUtils;
import com.fcdnipro.dniprolab.web.rest.dto.MessageDTO;
import com.fcdnipro.dniprolab.web.rest.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Message.
 */
@Service
@Transactional
public class MessageService {

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MessageMapper messageMapper;

    @Inject
    private MessageSearchRepository messageSearchRepository;

    /**
     * Save a message.
     * @return the persisted entity
     */
    public MessageDTO save(MessageDTO messageDTO) {
        log.debug("Request to save Message : {}", messageDTO);
        Message message = messageMapper.messageDTOToMessage(messageDTO);
        message = messageRepository.save(message);
        MessageDTO result = messageMapper.messageToMessageDTO(message);
        messageSearchRepository.save(message);
        return result;
    }

    /**
     *  get all the messages.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Message> findAll(Pageable pageable) {
        log.debug("Request to get all Messages");
        Page<Message> result = messageRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one message by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MessageDTO findOne(Long id) {
        log.debug("Request to get Message : {}", id);
        Message message = messageRepository.findOne(id);
        MessageDTO messageDTO = messageMapper.messageToMessageDTO(message);
        return messageDTO;
    }

    /**
     *  delete the  message by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Message : {}", id);
        messageRepository.delete(id);
        messageSearchRepository.delete(id);
    }

    /**
     * search for the message corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<MessageDTO> search(String query) {

        log.debug("REST request to search Messages for query {}", query);
        return StreamSupport
            .stream(messageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(messageMapper::messageToMessageDTO)
            .collect(Collectors.toList());
    }

    /*
    *  search for all messages for current user
    *  @param login - login of current user
    *  @return list of entities
     */
    @Transactional(readOnly = true)
    public List<Message> findAllMessagesForCurrentUser(String login){

        log.debug("Invocation findAllMessagesForCurrentUser method with login = {}", login);
        List<Message> result = messageRepository.findAllMessagesForCurrentUser(login);
        return result;
    }
}
