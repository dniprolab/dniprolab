package com.fcdnipro.dniprolab.smsNotification;

import com.fcdnipro.dniprolab.domain.User;
import com.fcdnipro.dniprolab.repository.UserRepository;
import com.fcdnipro.dniprolab.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Service class which gives opportunity send sms notifications to user's mobile phone,
 * get balance on service account, get status of notification (in this partial case vendor is turbosms.com.ua)
 */
@Service
public class SmsNotificationTurboSmsImpl implements SmsNotification {

    private final static Logger logger = LoggerFactory.getLogger(SmsNotificationTurboSmsImpl.class);

    private final static String NEW_MESSAGE_TYPE = "message";
    private final static String NEW_VIDEO_TYPE = "video";
    private final static String NEW_ADVERTISEMENT_TYPE = "advertisement";


    @Inject
    private UserRepository userRepository;

    @Inject
    private static MessageSource messageSource;

    @Inject
    private RequestBuilder requestBuilder;

    private boolean serviceEnabled;

    /*
    * Send sms
     */
    @Override
    public String notifyUser(String type) {
        if(serviceEnabled = false){
            throw new RuntimeException("Service disabled!");
        }
        String login = SecurityUtils.getCurrentUserLogin();
        User user = userRepository.findOneByLogin(login).get();
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        String request = getRequestStringToSendSms(type, locale, login);
        String response = requestBuilder.doXMLQuery(request);
        return null;
    }

    @Override
    public String getBalance() {
        String request = getRequestStringToGetBalance();
        String response = requestBuilder.doXMLQuery(request);
        return response;
    }

    @Override
    public String getStatus(String msgId) {
        String request = getRequestStringToGetSmsStatus(msgId);
        String response = requestBuilder.doXMLQuery(request);
        return response;
    }

    private static String getRequestStringToSendSms(String type, Locale locale, String login){
        String text = null;
        switch (type){
            case NEW_ADVERTISEMENT_TYPE : text = messageSource.getMessage("sms.notification.newAdvertisement", null, locale);
            case NEW_MESSAGE_TYPE : text = messageSource.getMessage("sms.notification.newMessage", null, locale);
            case NEW_VIDEO_TYPE : text = messageSource.getMessage("sms.notification.newVideo", null, locale);
        }
        return null;
    }

    private static String getRequestStringToGetBalance(){
        return null;
    }

    private static String getRequestStringToGetSmsStatus(String msgId){
        return null;
    }
}
