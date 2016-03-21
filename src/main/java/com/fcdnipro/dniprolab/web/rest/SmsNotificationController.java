package com.fcdnipro.dniprolab.web.rest;

import com.fcdnipro.dniprolab.smsNotification.SmsNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * REST service which allows get sms pool service balance and particular message status
 */
@RestController
@RequestMapping("/api")
public class SmsNotificationController {

    private final static Logger logger = LoggerFactory.getLogger(SmsNotificationController.class);

    @Inject
    private SmsNotificationService smsNotification;

    /*
    * GET current balance
     */
    @RequestMapping(value = "/sms-balance", method = RequestMethod.GET,
                                                    produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getAccountBalance(){
        logger.debug("REST request to get account balance");
        String result = smsNotification.getBalance();
        return ResponseEntity.ok()
            .body(result);
    }

    /*
    * POST message status
    * @param message id
     */
    @RequestMapping(value = "/sms-status", method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getMessageStatus(String msgId){
        logger.debug("REST request to get message status, id: ", msgId);
        String result = smsNotification.getMessageStatus(msgId);
        return ResponseEntity.ok()
            .body(result);
    }
}
