package com.radioteria.backing.mail;

public interface EmailService<L extends Letter> {
    void sendLetter(L letter);
}
