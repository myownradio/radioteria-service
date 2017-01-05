package com.radioteria.support.mail;

import com.radioteria.support.template.TemplateWithContext;

public interface EmailService {
    void sendPlainLetter(String to, String subject, String body);
    void sendTemplateBasedLetter(String to, String subject, TemplateWithContext templateWithContext);
}
