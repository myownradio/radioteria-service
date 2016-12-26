package com.radioteria.business.services.user.api;

import com.radioteria.data.entities.User;

public interface UserService {
    User findByEmail(String email);
    User findById(Long id);

    boolean passwordMatches(User user, String plainPassword);
    boolean isEmailAvailable(String email);
    void register(String email, String plainPassword, String name);
    void activateByEmail(String email);
    void changePassword(User user, String newPlainPassword);
    void deactivate(User user);
    void delete(User user);
}
