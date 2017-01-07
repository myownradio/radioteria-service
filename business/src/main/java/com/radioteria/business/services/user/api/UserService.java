package com.radioteria.business.services.user.api;

import com.radioteria.db.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    boolean isPasswordMatch(String plainPassword, User user);
    boolean isEmailAvailable(String email);

    void register(String email, String plainPassword, String name);
    void activateByEmail(String email);
    void changePassword(User user, String newPlainPassword);
    void deactivate(User user);
    void delete(User user);
}
