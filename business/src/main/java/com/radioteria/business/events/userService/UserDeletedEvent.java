package com.radioteria.business.events.userService;

import com.radioteria.db.entities.User;
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
