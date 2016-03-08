package com.fcdnipro.dniprolab.smsnotification;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

/**
 * Created by Overlord on 08.03.2016.
 */
public class SmsNotificationTest {

    @Mock
    private RequestBuilder requestBuilder;

    @Inject
    TurboSmsVendorConnection turboSmsVendorConnection;

    SmsNotificationTurboSmsImpl turboSms;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        turboSms = new SmsNotificationTurboSmsImpl();
        when(requestBuilder.doXMLQuery(anyString())).thenReturn("<?xml version=\"1.0\" encoding=\"UTF-8\"?><RESPONSE><status>1</status><credits>0.682</credits></RESPONSE>");

        turboSms.setServiceEnabled(true);

        ReflectionTestUtils.setField(turboSms, "requestBuilder", requestBuilder);
    }

    @Test
    public void testDeliveryOrBalanceReport(){
        assertThat(turboSms.notifyUser("message")).isEqualTo("Delivery status is: Message was delivered successfully.Available credits: 0.628");
    }

}
