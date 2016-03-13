package com.fcdnipro.dniprolab.smsnotification;


public interface SmsNotification {

    public String notifyUser(NotificationType type);

    public String getBalance();

    public String getMessageStatus(String msgId);

}
