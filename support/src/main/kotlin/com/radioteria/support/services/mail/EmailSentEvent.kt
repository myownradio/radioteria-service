package com.radioteria.support.services.mail

import org.springframework.context.ApplicationEvent

class EmailSentEvent(source: Any?, val emailData: Map<String, String?>) : ApplicationEvent(source)