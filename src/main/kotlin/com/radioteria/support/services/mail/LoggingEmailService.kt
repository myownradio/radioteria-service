package com.radioteria.support.services.mail

import org.slf4j.LoggerFactory

class LoggingEmailService : EmailService {

    override fun send(to: String, subject: String, body: String) {
        LOGGER.info("==== LETTER ====")
        LOGGER.info("To:      " + to)
        LOGGER.info("Subject: " + subject)
        LOGGER.info("Body:    " + body)
        LOGGER.info("================")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LoggingEmailService::class.java)
    }

}
