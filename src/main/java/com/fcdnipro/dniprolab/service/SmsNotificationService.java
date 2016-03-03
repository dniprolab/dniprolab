package com.fcdnipro.dniprolab.service;

import com.fcdnipro.dniprolab.domain.User;
import com.fcdnipro.dniprolab.repository.UserRepository;
import com.fcdnipro.dniprolab.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Notification via sms using SOAP to connect with provider.
 */
@Service
public class SmsNotificationService implements SmsNotification {

    private final static Logger log = LoggerFactory.getLogger(SmsNotificationService.class);

    @Inject
    private MessageSource messageSource;

    @Inject
    private UserRepository userRepository;

    private boolean status;

    @Override
    public void notifyUser(String messageType) {

        String login = SecurityUtils.getCurrentUserLogin();

        User user = userRepository.findOneByLogin(login).get();


    }
}
