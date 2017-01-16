package com.radioteria.business.services.user.api;

public interface PasswordRecoveryService {
    void sendPasswordRecoveryLetter(User user);
    void verifyPasswordRecoveryCode(String code);
    void changePasswordUsingRecoveryCode(String code, String newPassword);
}
