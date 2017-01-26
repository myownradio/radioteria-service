package com.radioteria.support.services.mail

import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Primary
@Profile("test")
@Service
class EventEmailServiceImpl(val eventPublisher: ApplicationEventPublisher) : EmailService {
    override fun send(to: String, subject: String, body: String) {
        eventPublisher.publishEvent(EmailSentEvent(this, mapOf(
                "to" to to,
                "subject" to subject,
                "body" to body
        )))
    }
}