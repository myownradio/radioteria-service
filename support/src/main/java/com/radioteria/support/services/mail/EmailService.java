package com.radioteria.support.services.mail;

public interface EmailService {
    void send(String to, String subject, String body);
}
