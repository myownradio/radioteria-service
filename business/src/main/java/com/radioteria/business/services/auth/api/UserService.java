package com.radioteria.business.services.auth.api;

import com.radioteria.business.services.auth.exceptions.UserExistsAuthServiceException;
import com.radioteria.business.services.auth.exceptions.UserNotFoundAuthServiceException;

public interface UserService {
    void register(String email, String plainPassword, String name) throws UserExistsAuthServiceException;
    void confirm(String email) throws UserNotFoundAuthServiceException;
}
