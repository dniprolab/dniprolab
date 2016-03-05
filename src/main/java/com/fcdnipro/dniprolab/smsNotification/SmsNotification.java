package com.fcdnipro.dniprolab.smsNotification;


public interface SmsNotification {

    public String notifyUser(String type);

    public String getBalance();

    public String getStatus(String msgId);

}
