package com.radioteria.business.services.auth.events;


import com.radioteria.data.entities.User;
import org.springframework.context.ApplicationEvent;

public class PasswordChangedEvent extends ApplicationEvent{

    private User user;

    public PasswordChangedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
