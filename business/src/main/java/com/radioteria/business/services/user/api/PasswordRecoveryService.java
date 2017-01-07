package com.radioteria.business.services.user.api;

import com.radioteria.db.entities.User;

public interface PasswordRecoveryService {
    void sendPasswordRecoveryLetter(User user);
    void verifyPasswordRecoveryCode(String code);
    void changePasswordUsingRecoveryCode(String code, String newPassword);
}
