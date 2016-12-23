package com.radioteria.business.services.auth.api;

import com.radioteria.business.services.auth.exceptions.UserExistsException;
import com.radioteria.business.services.auth.exceptions.UserNotFoundException;

public interface UserService {
    void register(String email, String plainPassword, String name) throws UserExistsException;
    void confirm(String email) throws UserNotFoundException;
}
