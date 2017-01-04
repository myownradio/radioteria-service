package com.radioteria.backing.mail;

import com.radioteria.backing.template.TemplateWithContext;
import org.springframework.stereotype.Service;

@Service
public class LogEmailService implements EmailService {
    @Override
    public void sendPlainLetter(String to, String subject, String body) {

    }

    @Override
    public void sendTemplateBasedLetter(String to, String subject, TemplateWithContext templateWithContext) {

    }
}
