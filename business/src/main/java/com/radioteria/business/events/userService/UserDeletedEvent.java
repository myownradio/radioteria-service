package com.radioteria.business.events.userService;

import org.springframework.context.ApplicationEvent;

public class UserDeletedEvent extends ApplicationEvent {

    private User user;

    public UserDeletedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
