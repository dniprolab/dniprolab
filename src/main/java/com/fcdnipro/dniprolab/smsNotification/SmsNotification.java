package com.fcdnipro.dniprolab.smsnotification;


public interface SmsNotification {

    public void notifyUser(String type);

    public String getBalance();

    public String getStatus(String msgId);

}
