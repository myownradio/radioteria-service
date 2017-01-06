package com.radioteria.support.services.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingEmailService implements EmailService {
    final private static Logger LOGGER = LoggerFactory.getLogger(LoggingEmailService.class);

    @Override
    public void send(String to, String subject, String body) {
        LOGGER.info("==== LETTER ====");
        LOGGER.info("To:      " + to);
        LOGGER.info("Subject: " + subject);
        LOGGER.info("Body:    " + body);
        LOGGER.info("================");
    }
}
