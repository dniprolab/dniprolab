package com.fcdnipro.dniprolab.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fcdnipro.dniprolab.domain.Message;
import com.fcdnipro.dniprolab.service.MessageService;
import com.fcdnipro.dniprolab.web.rest.util.HeaderUtil;
import com.fcdnipro.dniprolab.web.rest.util.PaginationUtil;
import com.fcdnipro.dniprolab.web.rest.dto.MessageDTO;
import com.fcdnipro.dniprolab.web.rest.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Message.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    @Inject
    private MessageService messageService;

    @Inject
    private MessageMapper messageMapper;

    /**
     * POST  /messages -> Create a new message.
     */
    @RequestMapping(value = "/messages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageDTO> createMessage(@Valid @RequestBody MessageDTO messageDTO) throws URISyntaxException {
        log.debug("REST request to save Message : {}", messageDTO);
        if (messageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("message", "idexists", "A new message cannot already have an ID")).body(null);
        }
        MessageDTO result = messageService.save(messageDTO);
        return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("message", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /messages -> Updates an existing message.
     */
    @RequestMapping(value = "/messages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageDTO> updateMessage(@Valid @RequestBody MessageDTO messageDTO) throws URISyntaxException {
        log.debug("REST request to update Message : {}", messageDTO);
        if (messageDTO.getId() == null) {
            return createMessage(messageDTO);
        }
        MessageDTO result = messageService.save(messageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("message", messageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /messages -> get all the messages.
     */
    @RequestMapping(value = "/messages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MessageDTO>> getAllMessages(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Messages");
        Page<Message> page = messageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/messages");
        return new ResponseEntity<>(page.getContent().stream()
            .map(messageMapper::messageToMessageDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }
    /*
    * GET /messages/:login - get all Message entities for current user
    * @param - PathVariable login of current user
     */
    @RequestMapping(value = "/messages/{login}", method = RequestMethod.GET,
                                                produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MessageDTO>> getAllMessagesForCurrentUser(@PathVariable String login, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get all Message for current user");
        List<Message> messages = messageService.findAllMessagesForCurrentUser(login);
        PageImpl<Message> messagePage = new PageImpl<Message>(messages);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(messagePage, "/api/messages" + login);
        return new ResponseEntity<>(messagePage.getContent().stream()
            .map(messageMapper::messageToMessageDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);

    }

    /**
     * GET  /messages/:id -> get the "id" message.
     */
    @RequestMapping(value = "/messages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageDTO> getMessage(@PathVariable Long id) {
        log.debug("REST request to get Message : {}", id);
        MessageDTO messageDTO = messageService.findOne(id);
        return Optional.ofNullable(messageDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /messages/:id -> delete the "id" message.
     */
    @RequestMapping(value = "/messages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        log.debug("REST request to delete Message : {}", id);
        messageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("message", id.toString())).build();
    }

    /**
     * SEARCH  /_search/messages/:query -> search for the message corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/messages/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MessageDTO> searchMessages(@PathVariable String query) {
        log.debug("Request to search Messages for query {}", query);
        return messageService.search(query);
    }
}
