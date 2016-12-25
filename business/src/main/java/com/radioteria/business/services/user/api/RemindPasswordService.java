package com.radioteria.business.services.user.api;

import com.radioteria.data.entities.User;

public interface RemindPasswordService {
    void sendRemindPasswordLetter(User user);
    void verifyPasswordRecoveryCode(String code);
}
