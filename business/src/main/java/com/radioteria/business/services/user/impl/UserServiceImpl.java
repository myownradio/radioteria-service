package com.radioteria.business.services.user.impl;

import com.radioteria.business.events.userService.PasswordChangedEvent;
import com.radioteria.business.events.userService.UserConfirmedEvent;
import com.radioteria.business.events.userService.UserRegisteredEvent;
import com.radioteria.business.services.user.api.UserService;
import com.radioteria.business.services.user.exceptions.UserExistsException;
import com.radioteria.business.services.user.exceptions.UserNotFoundException;
import com.radioteria.db.dao.api.UserDao;
import com.radioteria.db.entities.User;
import com.radioteria.db.enumerations.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    @Value("${registration.email.verify.enabled}")
    private boolean emailVerifyEnabled = true;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           ApplicationEventPublisher eventPublisher) {

        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;

    }

    @Override
    public Optional<User> findByEmail(String email) {

        return userDao.findByEmail(email);

    }

    @Override
    public Optional<User> findById(Long id) {

        return userDao.find(id);

    }

    public boolean passwordMatches(User user, String plainPassword) {

        return passwordEncoder.matches(plainPassword, user.getPassword());

    }

    public boolean isEmailAvailable(String email) {

        return userDao.isEmailAvailable(email);

    }

    public void register(String email, String plainPassword, String name) {

        throwErrorIfEmailNotAvailable(email);

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

    public void activateByEmail(String email) {

        User user = findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email \"%s\" not exists.", email)));

        user.setState(UserState.ACTIVE);

        publishEventAboutUserConfirmed(user);

    }

    public void changePassword(User user, String newPlainPassword) {

        String encodedPassword = passwordEncoder.encode(newPlainPassword);

        user.setPassword(encodedPassword);

        publishEventAboutChangedPassword(user);

    }

    public void deactivate(User user) {

        user.setState(UserState.DELETED);

        publishEventAboutUserDeleted(user);

    }

    public void delete(User user) {

        throw new IllegalStateException("No, God! Please, No!");

    }

    private void throwErrorIfEmailNotAvailable(String email) {

        if (!isEmailAvailable(email)) {
            throw new UserExistsException(String.format("User with email \"%s\" already exists.", email));
        }

    }

    private void publishEventAboutUserRegistered(User user) {

        ApplicationEvent event = new UserRegisteredEvent(this, user);

        eventPublisher.publishEvent(event);

    }

    private void publishEventAboutUserConfirmed(User user) {

        ApplicationEvent event = new UserConfirmedEvent(this, user);

        eventPublisher.publishEvent(event);

    }

    private void publishEventAboutUserDeleted(User user) {

        ApplicationEvent event = new UserConfirmedEvent(this, user);

        eventPublisher.publishEvent(event);

    }

    private void publishEventAboutChangedPassword(User user) {

        ApplicationEvent event = new PasswordChangedEvent(this, user);

        eventPublisher.publishEvent(event);

    }
}
