package com.radioteria.business.services.auth.impl;

import com.radioteria.business.events.UserRegisteredEvent;
import com.radioteria.business.services.auth.api.UserRegistrationService;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import com.radioteria.data.enumerations.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserRegistrationServiceImpl(UserDao userDao,
                                       PasswordEncoder passwordEncoder,
                                       ApplicationEventPublisher eventPublisher) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public void register(String email, String plainPassword, String name) {

        String encodedPassword = passwordEncoder.encode(plainPassword);

        User user = new User();

        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setName(name);
        user.setState(UserState.ACTIVE);
        user.setAvatarFile(null);

        userDao.persist(user);

        publishEventAboutUserRegistered(user);

    }

    private void publishEventAboutUserRegistered(User registeredUser) {

        ApplicationEvent event = new UserRegisteredEvent(this, registeredUser);

        eventPublisher.publishEvent(event);

    }

    public void confirm(String email) {

        User user = userDao.findByEmail(email);

        user.setState(UserState.ACTIVE);

    }

}
