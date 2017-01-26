package com.radioteria.support.services.mail

import org.springframework.context.ApplicationEvent

class SendEmailEvent(source: Any?, val mailData: Map<String, String?>) : ApplicationEvent(source)