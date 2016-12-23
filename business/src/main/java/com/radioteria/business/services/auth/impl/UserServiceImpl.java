package com.radioteria.business.services.auth.impl;

import com.radioteria.business.events.UserConfirmedEvent;
import com.radioteria.business.events.UserRegisteredEvent;
import com.radioteria.business.services.auth.api.UserService;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import com.radioteria.data.enumerations.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    private boolean emailVerifyEnabled = true;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           ApplicationEventPublisher eventPublisher) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Value("${registration.email.verify.enabled}")
    public void setEmailVerifyEnabled(boolean emailVerifyEnabled) {
        this.emailVerifyEnabled = emailVerifyEnabled;
    }

    public void register(String email, String plainPassword, String name) {

        String encodedPassword = passwordEncoder.encode(plainPassword);

        User user = new User();

        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setName(name);
        user.setState(emailVerifyEnabled ? UserState.INACTIVE : UserState.ACTIVE);
        user.setAvatarFile(null);

        userDao.persist(user);

        publishEventAboutUserRegistered(user);

    }

    public void confirm(String email) {

        User user = userDao.findByEmail(email);

        user.setState(UserState.ACTIVE);

        publishEventAboutUserConfirmed(user);

    }

    private void publishEventAboutUserRegistered(User user) {

        ApplicationEvent event = new UserRegisteredEvent(this, user);

        eventPublisher.publishEvent(event);

    }

    private void publishEventAboutUserConfirmed(User user) {

        ApplicationEvent event = new UserConfirmedEvent(this, user);

        eventPublisher.publishEvent(event);

    }
}
