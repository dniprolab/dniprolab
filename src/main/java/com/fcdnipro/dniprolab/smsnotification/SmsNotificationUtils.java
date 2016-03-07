package com.fcdnipro.dniprolab.smsnotification;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilize class for processing request "raw" string to well-read form
 */
public final class SmsNotificationUtils {

    private final static String STATUS_PREFIX = "<status>";
    private final static String STATUS_SUFFIX = "</status>";

    private final static String CREDITS_PREFIX = "<credits>";
    private final static String CREDITS_SUFFIX = "</credits>";

    private final static String SENT_DATE = "sentdate=";
    private final static String DONE_DATE = "donedate=";
    private final static String STATUS = "status=";

    private static final Map<String, String> deliveryMessageStatusCodes = new HashMap<>();
    static {
        deliveryMessageStatusCodes.put("-1", "Authentication failed.");
        deliveryMessageStatusCodes.put("-2", "Wrong XML syntax.");
        deliveryMessageStatusCodes.put("-3", "Not enough credits on account balance.");
        deliveryMessageStatusCodes.put("1", "Message was delivered successfully.");
        deliveryMessageStatusCodes.put("0", "Request balance status is OK");
    }

    private static final Map<String, String> messageStatuses = new HashMap<>();
    {
        messageStatuses.put("SENT", "Message was sent.");
        messageStatuses.put("NOT_DELIVERED", "Message wasn't delivered.");
        messageStatuses.put("DELIVERED", "Message was delivered");
        messageStatuses.put("INVALID_DESTINATION_ADDRESS", "Destination address invalid.");
        messageStatuses.put("INVALID_SOURCE_ADDRESS", "Source address invalid");
        messageStatuses.put("NOT_ALLOWED", "Recipient mobile operator is out of processing.");
        messageStatuses.put("NOT_ENOUGH_CREDITS", "Not enough credits on account balance.");
    }

    private final static String DELIVERY_STATUS = "Delivery status is: ";
    private final static String AVAILABLE_CREDITS = "Available credits: ";

    private final static String SEND_DATE_TEXT = "Message send date: ";
    private final static String DONE_DATE_TEXT = "Message delivered at: ";
    private final static String STATUS_TEXT = "Delivery status is: ";

    /*
     * Convert raw response with number codes after string after message sending to well-read form
     * @param serverResponse - raw string from server
     * @return string in well-read form
     */
    public static String parseDeliveryOrBalanceReport(String serverResponse){
        //typical service response is: <?xml version="1.0" encoding="UTF-8"?><RESPONSE><status>2</status><credits>0.682</credits></RESPONSE>
        //AUTH_FAILED -1 Wrong login or password
        //XML_ERROR -2 Wrong XML syntax
        //NOT_ENOUGH_CREDITS -3 Not enough credits on account balance
        //NO_RECIPIENTS -4 Missing correct recipient number
        //SEND_OK  >0 count of received messages, in this case should always be 1
        //SEND_OK 0 request to get current credit balance on service account

        String statusCode = serverResponse.substring(serverResponse.indexOf(STATUS_PREFIX) + STATUS_PREFIX.length(),
                                                                            serverResponse.indexOf(STATUS_SUFFIX));
        String availableCredits = serverResponse.substring(serverResponse.indexOf(CREDITS_PREFIX) + CREDITS_PREFIX.length(),
                                                                            serverResponse.indexOf(CREDITS_SUFFIX));
        String convertedStatusForm = deliveryMessageStatusCodes.get(statusCode);
        return DELIVERY_STATUS + convertedStatusForm + AVAILABLE_CREDITS + availableCredits;
    }

    /*
    * Parses string with "raw" response to well-read form
    * @param messageStatusResponse - response string
     */
    public static String parseMessageStatusResponse(String messageStatusResponse){
        //typical service response is: <?xml version="1.0" encoding="UTF-8"?><deliveryreport><message id="1299" sentdate="0000-00-00 00:00:00" donedate="0000-00-00 00:00:00" status="0" /></deliveryreport>

        //SENT Was sent
        //NOT_DELIVERED Wasn't delivered
        //DELIVERED Delivered
        //NOT_ALLOWED Operator is not allowed
        //INVALID_DESTINATION_ADDRESS Wrong destination address
        //INVALID_SOURCE_ADDRESS Wrong source name
        //NOT_ENOUGH_CREDITS Not enough credits
        String send_date = messageStatusResponse.substring(messageStatusResponse.indexOf(SENT_DATE) + SENT_DATE.length(),
                                                            messageStatusResponse.indexOf(SENT_DATE) + SENT_DATE.length() + 21);
        String done_date = messageStatusResponse.substring(messageStatusResponse.indexOf(DONE_DATE) + DONE_DATE.length(),
                                                            messageStatusResponse.indexOf(DONE_DATE) + DONE_DATE.length() + 21);
        String status_code = messageStatusResponse.substring(messageStatusResponse.indexOf(STATUS) + STATUS.length(),
                                                            messageStatusResponse.lastIndexOf("\""));

        String status = messageStatuses.get(status_code);
        return SEND_DATE_TEXT + send_date + DONE_DATE_TEXT + done_date + STATUS_TEXT + status;
    }
}
