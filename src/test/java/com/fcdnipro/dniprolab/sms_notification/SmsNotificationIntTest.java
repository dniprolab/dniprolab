package com.fcdnipro.dniprolab.sms_notification;

import com.fcdnipro.dniprolab.Application;
import com.fcdnipro.dniprolab.config.JHipsterProperties;
import com.fcdnipro.dniprolab.repository.UserRepository;
import com.fcdnipro.dniprolab.service.UserService;
import com.fcdnipro.dniprolab.smsNotification.NotificationType;
import com.fcdnipro.dniprolab.smsNotification.RequestBuilder;
import com.fcdnipro.dniprolab.smsNotification.SmsNotificationServiceEpochtaImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

import static org.assertj.core.api.StrictAssertions.assertThat;

/**
 * Created by Overlord on 11.03.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class SmsNotificationIntTest {

    @Inject
    private UserService userService;

    @Inject
    private MessageSource messageSource;

    @Mock
    private RequestBuilder requestBuilder;

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    UserRepository userRepository;

    private SmsNotificationServiceEpochtaImpl smsNotification;

    private static final String GET_BALANCE_QUERY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SMS><operations><operation>BALANCE</operation></operations><authentification><username>mock</username><password>mock</password></authentification></SMS>";
    private static final String GET_MESSAGE_STATUS_QUERY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SMS><operations><operation>GETSTATUS</operation></operations><authentification><username>mock</username><password>mock</password></authentification><statistics><messageid>999</messageid></statistics></SMS>";
    private static final String GET_SEND_SMS_QUERY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SMS><operations><operation>SEND</operation></operations><authentification><username>mock</username><password>mock</password></authentification><message><sender>dnipro.lab</sender><text>You have got new message! Please, check your account to read it.</text></message><numbers>+380505145255</numbers></SMS>";
    private static final String MESSAGE_ID = "999";
    private static final NotificationType MESSAGE_TYPE = NotificationType.MESSAGE;
    private static final String SERVER_BALANCE_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><RESPONSE><status>0</status><credits>0.682</credits></RESPONSE>";
    private static final String BALANCE_STRING = "Delivery status is: Request balance status is OK. Available credits: 0.682";

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);


        smsNotification = new SmsNotificationServiceEpochtaImpl();
        ReflectionTestUtils.setField(smsNotification, "userService", userService);
        ReflectionTestUtils.setField(smsNotification, "jHipsterProperties", jHipsterProperties);
        ReflectionTestUtils.setField(smsNotification, "messageSource", messageSource);
        ReflectionTestUtils.setField(smsNotification, "requestBuilder", requestBuilder);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldReturnCorrectStringWithBalanceXMLQuery() throws Exception {
        String expectedQuery = Whitebox.invokeMethod(smsNotification, "getRequestStringToGetBalance");
        Assert.assertEquals(expectedQuery, GET_BALANCE_QUERY);
    }
    @Test
    public void shouldReturnCorrectStringWithMessageStatusXMLQuery() throws Exception {
        String expectedQuery = Whitebox.invokeMethod(smsNotification, "getRequestStringToGetSmsStatus", MESSAGE_ID);
        Assert.assertEquals(expectedQuery, GET_MESSAGE_STATUS_QUERY);
    }
    @Test
    public void shouldReturnCorrectStringWithMessageSendQuery() throws Exception {

        String expectedQuery = Whitebox.invokeMethod(smsNotification, "getRequestStringToSendSms", MESSAGE_TYPE);
        Assert.assertEquals(expectedQuery, GET_SEND_SMS_QUERY);
    }

    @Test
    public void assertThatReturnsParsedString() {
        when(requestBuilder.doXMLQuery(GET_BALANCE_QUERY)).thenReturn(SERVER_BALANCE_RESPONSE);
        String expectedBalance = smsNotification.getBalance();
        Assert.assertEquals(expectedBalance, BALANCE_STRING);
    }
}
