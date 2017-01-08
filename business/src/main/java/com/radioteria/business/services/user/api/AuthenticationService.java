package com.radioteria.business.services.user.api;

import com.radioteria.db.entities.User;
import org.springframework.security.authentication.BadCredentialsException;

public interface AuthenticationService {
    void login(String login, String password) throws BadCredentialsException;
    void login(User user);
    void logout();
}
