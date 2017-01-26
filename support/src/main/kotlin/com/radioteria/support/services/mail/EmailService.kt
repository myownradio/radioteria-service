package com.radioteria.support.services.mail

interface EmailService {
    fun send(to: String, subject: String, body: String)
}
