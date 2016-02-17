package com.fcdnipro.dniprolab.service;

import com.fcdnipro.dniprolab.Application;
import com.fcdnipro.dniprolab.domain.Message;
import com.fcdnipro.dniprolab.domain.User;
import com.fcdnipro.dniprolab.repository.MessageRepository;
import com.fcdnipro.dniprolab.repository.UserRepository;
import com.fcdnipro.dniprolab.web.rest.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.assertj.core.api.Assertions.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by Overlord on 17.02.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MessageServiceTest {

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MessageService messageService;

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    private Message message;

    private User user;

    private static final String DEFAULT_TEXT = "";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";

    private static final byte[] DEFAULT_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final String DEFAULT_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String DEFAULT_AUTHOR = "AAAAA";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final String DEFAULT_TITLE = "AAAAA";

    @PostConstruct
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void initTest() {

        user = userService.getUserWithAuthoritiesByLogin("admin").get();

        message = new Message();
        message.setText(DEFAULT_TEXT);
        message.setImage(DEFAULT_IMAGE);
        message.setImageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        message.setDocument(DEFAULT_DOCUMENT);
        message.setDocumentContentType(DEFAULT_DOCUMENT_CONTENT_TYPE);
        message.setAuthor(DEFAULT_AUTHOR);
        message.setCreated(DEFAULT_CREATED);
        message.setTitle(DEFAULT_TITLE);
        message.setUser(user);

    }
    /*
    * Should return correct message entity
     */

    @Test
    public void assertThatUserServiceReturnsMessage(){

        messageRepository.saveAndFlush(message);

        List<Message> messages = messageService.findAllMessagesForCurrentUser(user.getLogin());

        assertThat(messages.size()).isEqualTo(1);
        assertThat(messages.get(0).getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(messages.get(0).getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(messages.get(0).getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(messages.get(0).getDocument()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(messages.get(0).getText()).isEqualTo(DEFAULT_TEXT);
    }

}
