package com.fcdnipro.dniprolab.smsNotification;

import com.fcdnipro.dniprolab.config.JHipsterProperties;
import com.fcdnipro.dniprolab.domain.User;
import com.fcdnipro.dniprolab.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Service class which gives opportunity send sms notifications to user's mobile phone,
 * get balance on service account, get status of notification (in this partial case vendor is turbosms.com.ua)
 */
@Service
@Transactional
public class SmsNotificationServiceEpochtaImpl implements SmsNotificationService {

    private final static Logger logger = LoggerFactory.getLogger(SmsNotificationServiceEpochtaImpl.class);

    @Inject
    UserService userService;

    @Inject
    private MessageSource messageSource;

    @Inject
    private com.fcdnipro.dniprolab.smsNotification.RequestBuilder requestBuilder;

    private boolean serviceEnabled = false;

    @Inject
    private JHipsterProperties jHipsterProperties;

    private final static String WRONG_MESSAGE_TYPE = "Wrong message type";
    private final static String SERVICE_DISABLED = "Sms notification service disabled";

    /*
    * Top level method to notify user by sending him sms
    * Invoking service classes to get xml query, establish connection,
    * send request and process response in well-read form
    * Result should be logged for further control by administrator
    * @param type - type of message(new message or video etc)
    * @return string with response in well-read form or 'disabled' message
     */
    @Override
    public String notifyUser(NotificationType notificationType) {
        if(serviceEnabled == false){
            return SERVICE_DISABLED;
        }
        logger.debug("Invoke notifyUser method, message type {}.", notificationType.toString());
        String request = this.getRequestStringToSendSms(notificationType);
        logger.info("Sms notification request has been sent to service");
        String rawResponse = requestBuilder.doXMLQuery(request);
        String parsedResponse = EpochtaUtils.parseDeliveryOrBalanceReport(rawResponse);
        return parsedResponse;
    }

    /*
    * Top-level method for getting current account balance of sms service
    * @return parsed response - response in well-read form
     */
    @Override
    public String getBalance() {
        String request = this.getRequestStringToGetBalance();
        String rawResponse = requestBuilder.doXMLQuery(request);
        return EpochtaUtils.parseDeliveryOrBalanceReport(rawResponse);
    }

    /*
    * Top-level method for getting status of particular sms
    * @param msgId - sms id
    * @return response in well-read form
     */
    @Override
    public String getMessageStatus(String msgId) {
        String request = getRequestStringToGetSmsStatus(msgId);
        String rawResponse = requestBuilder.doXMLQuery(request);
        return EpochtaUtils.parseMessageStatusResponse(rawResponse);
    }

    /*
    * Generate a string with XML query for sending sms
    * @param notificationType - type of message
    * @return string with query
     */
    private String getRequestStringToSendSms(NotificationType notificationType){
        User user = userService.getCurrentUser();
        logger.debug("Generation query to send notification sms to user {}", user);
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        String telNumber = user.getTelNumber();
        String text = null;
        if(notificationType == NotificationType.ADVERTISEMENT) {
            text = messageSource.getMessage("sms.notification.newAdvertisement", null, locale);
        }
        else if(notificationType == NotificationType.MESSAGE){
            text = messageSource.getMessage("sms.notification.newMessage", null, locale);
        }
        else if(notificationType == NotificationType.VIDEO){
            text = messageSource.getMessage("sms.notification.newVideo", null, locale);
        }
        else {
            throw new RuntimeException(WRONG_MESSAGE_TYPE);
        }
        StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            sb.append("<SMS>");
            sb.append("<operations>");
            sb.append("<operation>SEND</operation>");
            sb.append("</operations>");
            sb.append("<authentification>");
            sb.append("<username>" + jHipsterProperties.getSmsNotificationProperties().getLogin() + "</username>");
            sb.append("<password>" + jHipsterProperties.getSmsNotificationProperties().getPassword() + "</password>");
            sb.append("</authentification>");
            sb.append("<message>");
            sb.append("<sender>" + jHipsterProperties.getSmsNotificationProperties().getApplicationName() + "</sender>");
            sb.append("<text>" + text + "</text>");
            sb.append("</message>");
            sb.append("<numbers>");
            sb.append(telNumber);
            sb.append("</numbers>");
            sb.append("</SMS>");
        return sb.toString();
    }
    /*
    * Generates a string with XML query for getting account balance
     */
    private String getRequestStringToGetBalance(){
        logger.debug("Generation query to get balance");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stringBuilder.append("<SMS>");
        stringBuilder.append("<operations>");
        stringBuilder.append("<operation>BALANCE</operation>");
        stringBuilder.append("</operations>");
        stringBuilder.append("<authentification>");
        stringBuilder.append("<username>" + jHipsterProperties.getSmsNotificationProperties().getLogin() + "</username>");
        stringBuilder.append("<password>" + jHipsterProperties.getSmsNotificationProperties().getPassword() + "</password>");
        stringBuilder.append("</authentification>");
        stringBuilder.append("</SMS>");
        return stringBuilder.toString();
    }

    /*
    * Creates a string with XML query for getting message status details
    * @param msgId - sms id
    * @return string with query
     */
    public String getRequestStringToGetSmsStatus(String msgId){
        logger.debug("Generation query to get message status, id: {}", msgId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stringBuilder.append("<SMS><operations><operation>GETSTATUS</operation></operations>");
        stringBuilder.append("<authentification>");
        stringBuilder.append("<username>" + jHipsterProperties.getSmsNotificationProperties().getLogin() + "</username>");
        stringBuilder.append("<password>" + jHipsterProperties.getSmsNotificationProperties().getPassword() + "</password>");
        stringBuilder.append("</authentification>");
        stringBuilder.append("<statistics>");
        stringBuilder.append("<messageid>" + msgId + "</messageid>");
        stringBuilder.append("</statistics>");
        stringBuilder.append("</SMS>");
        return stringBuilder.toString();
    }

    public SmsNotificationServiceEpochtaImpl() {
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setjHipsterProperties(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
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

    public JHipsterProperties getjHipsterProperties() {
        return jHipsterProperties;
    }

}
