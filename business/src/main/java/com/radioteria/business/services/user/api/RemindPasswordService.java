package com.radioteria.business.services.user.api;

import com.radioteria.db.entities.User;

public interface RemindPasswordService {
    void sendRemindPasswordLetter(User user);
    void verifyPasswordRecoveryCode(String code);
}
