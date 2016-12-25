package com.radioteria.backing.mail;

import com.radioteria.backing.template.TemplateWithContext;

public interface EmailService {
    void sendPlainLetter(String to, String subject, String body);
    void sendTemplateBasedLetter(String to, String subject, TemplateWithContext templateWithContext);
}
