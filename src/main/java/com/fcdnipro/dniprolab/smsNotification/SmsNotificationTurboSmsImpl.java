package com.fcdnipro.dniprolab.smsnotification;

import com.fcdnipro.dniprolab.config.JHipsterProperties;
import com.fcdnipro.dniprolab.domain.User;
import com.fcdnipro.dniprolab.repository.UserRepository;
import com.fcdnipro.dniprolab.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Locale;

//TODO add telephone number field to entity and web form
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

    private final static JHipsterProperties jHipsterProperties = new JHipsterProperties();
    private final static String serviceLogin = jHipsterProperties.getSmsNotification().getLogin();
    private final static String servicePassword = jHipsterProperties.getSmsNotification().getPassword();
    private final static String applicationName = jHipsterProperties.getSmsNotification().getApplicationName();

    private final static String WRONG_MESSAGE_TYPE = "Wrong message type";

    /*
    * Top level method to notify user by sending him sms
    * Invoking service classes to get xml query, establish connection,
    * send request and process response in well-read form
    * Result should be logged for further control by administrator
    * Current user getting from security utils class
    * @param type - type of message(new message or video etc)
    *
     */
    @Override
    public void notifyUser(String type) {
        if(serviceEnabled == false){
            return;
            }
        String login = SecurityUtils.getCurrentUserLogin();
        User user = userRepository.findOneByLogin(login).get();
        logger.debug("Invoke notifyUser method with user {}, message type {}", user.getLogin(), type);
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        String telNumber = user.getEmail();                     //TODO add new field to user entity!
        String request = getRequestStringToSendSms(type, locale, telNumber);
        logger.info("Sending sms notification request was sent to service, message recipient is user {}, message type is {}", user.getLogin(), type);
        String rawResponse = requestBuilder.doXMLQuery(request);
        String parsedResponse = SmsNotificationUtils.parseDeliveryOrBalanceReport(rawResponse);
        logger.info("Notification service response after sending request to notify user {} with message type {} is: {}", user.getLogin(), type, parsedResponse);
    }

    /*
    * Top-level method for getting current account balance of sms service
    * @return parsed response - response in well-read form
     */
    @Override
    public String getBalance() {
        String request = getRequestStringToGetBalance();
        String rawResponse = requestBuilder.doXMLQuery(request);
        String parsedResponse = SmsNotificationUtils.parseDeliveryOrBalanceReport(rawResponse);
        return parsedResponse;
    }

    /*
    * Top-level method for getting status of particular sms
    * @param msgId - sms id
    * @return response in well-read form
     */
    @Override
    public String getStatus(String msgId) {
        String request = getRequestStringToGetSmsStatus(msgId);
        String rawResponse = requestBuilder.doXMLQuery(request);
        String parsedResponse = SmsNotificationUtils.parseMessageStatusResponse(rawResponse);
        return parsedResponse;
    }

    /*
    * Create a string with XML query for sending sms
    * @param type - type of message
    * @param locale - user's locale
    * @param telNumber
    * @return string with query
     */
    private static String getRequestStringToSendSms(String type, Locale locale,
                                                    String telNumber){
        String text = null;
        switch (type){
            case NEW_ADVERTISEMENT_TYPE : text = messageSource.getMessage("sms.notification.newAdvertisement", null, locale);
            case NEW_MESSAGE_TYPE : text = messageSource.getMessage("sms.notification.newMessage", null, locale);
            case NEW_VIDEO_TYPE : text = messageSource.getMessage("sms.notification.newVideo", null, locale);
        }
        if(text.equals(null)){
            throw new RuntimeException(WRONG_MESSAGE_TYPE);
        }
        StringBuilder sb = new StringBuilder();
            sb.append("<SMS>");
            sb.append("<operations>");
            sb.append("<operation>SEND</operation>");
            sb.append("</operations>");
            sb.append("<authentification>");
            sb.append("<username>" + serviceLogin + "</username>");
            sb.append("<password>" + servicePassword + "</password>");
            sb.append("</authentification>");
            sb.append("<message>");
            sb.append("<sender>" + applicationName + "</sender>");
            sb.append("<text>" + text + "</text>");
            sb.append("</message>");
            sb.append("<numbers>");
            sb.append(telNumber);
            sb.append("</numbers>");
            sb.append("</SMS>");
        return sb.toString();
    }
    /*
    * Creates a string with XML query for getting account balance
     */
    private static String getRequestStringToGetBalance(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stringBuilder.append("<SMS>");
        stringBuilder.append("<operations>");
        stringBuilder.append("<operation>BALANCE</operation>");
        stringBuilder.append("</operations>");
        stringBuilder.append("<authentification>");
        stringBuilder.append("<username>" + serviceLogin + "</username>");
        stringBuilder.append("<password>" + servicePassword + "</password>");
        stringBuilder.append("</authentification> ");
        stringBuilder.append("</SMS>");
        return stringBuilder.toString();
    }

    /*
    * Creates a string with XML query for getting message status details
    * @param msgId - sms id
    * @return string with query
     */
    private static String getRequestStringToGetSmsStatus(String msgId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stringBuilder.append("<SMS><operations><operation>GETSTATUS</operation></operations>");
        stringBuilder.append("<authentification>");
        stringBuilder.append("<username>" + serviceLogin + "</username>");
        stringBuilder.append("<password>" + servicePassword + "</password>");
        stringBuilder.append("</authentification>");
        stringBuilder.append("<statistics>");
        stringBuilder.append("<messageid>" + msgId + "</messageid>");
        stringBuilder.append("</statistics>");
        stringBuilder.append("</SMS>");
        return stringBuilder.toString();
    }

    public SmsNotificationTurboSmsImpl() {
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static MessageSource getMessageSource() {
        return messageSource;
    }

    public static void setMessageSource(MessageSource messageSource) {
        SmsNotificationTurboSmsImpl.messageSource = messageSource;
    }

    public RequestBuilder getRequestBuilder() {
        return requestBuilder;
    }

    public void setRequestBuilder(RequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    public boolean isServiceEnabled() {
        return serviceEnabled;
    }

    public void setServiceEnabled(boolean serviceEnabled) {
        this.serviceEnabled = serviceEnabled;
    }

    public static JHipsterProperties getjHipsterProperties() {
        return jHipsterProperties;
    }
}
