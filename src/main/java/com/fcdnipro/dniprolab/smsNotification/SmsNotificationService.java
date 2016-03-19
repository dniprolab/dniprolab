package com.fcdnipro.dniprolab.smsNotification;


public interface SmsNotificationService {

    public String notifyUser(NotificationType type);

    public String getBalance();

    public String getMessageStatus(String msgId);

}
