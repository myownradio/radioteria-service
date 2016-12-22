package com.radioteria.business.services.auth.impl;

import com.radioteria.business.services.auth.api.UserRegistrationService;
import com.radioteria.business.services.auth.exceptions.UserExistsAuthServiceException;
import com.radioteria.business.services.auth.exceptions.UserNotFoundAuthServiceException;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import com.radioteria.data.enumerations.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private UserDao userDao;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrationServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String email, String plainPassword, String name) throws UserExistsAuthServiceException {

        throwExceptionIfEmailExists(email);

        String encodedPassword = passwordEncoder.encode(plainPassword);

        User user = new User();

        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setName(name);
        user.setState(UserState.ACTIVE);
        user.setAvatarFile(null);

        userDao.persist(user);

    }

    public void confirm(String email) throws UserNotFoundAuthServiceException {

        User user = userDao.findByEmail(email);

        throwErrorIfNoUser(user, email);

        user.setState(UserState.ACTIVE);

    }

    private void throwErrorIfNoUser(User user, String email) throws UserNotFoundAuthServiceException {

        if (user != null) {
            return;
        }

        throw new UserNotFoundAuthServiceException(
                String.format("User with email '%s' not found", email)
        );

    }

    private void throwExceptionIfEmailExists(String email) throws UserExistsAuthServiceException {

        if (userDao.isEmailAlreadyUsed(email)) {
            throw new UserExistsAuthServiceException(
                    String.format("User with email '%s' already exists", email)
            );
        }

    }

}
