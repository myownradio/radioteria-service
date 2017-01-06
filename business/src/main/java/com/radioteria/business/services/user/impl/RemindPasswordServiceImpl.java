package com.radioteria.business.services.user.impl;

import com.radioteria.business.services.misc.PasswordRecoveryCode;
import com.radioteria.business.services.user.api.UserService;
import com.radioteria.db.enumerations.UserState;
import com.radioteria.support.template.TemplateService;
import com.radioteria.business.services.user.api.RemindPasswordService;
import com.radioteria.business.services.user.exceptions.RemindPasswordServiceException;
import com.radioteria.db.dao.api.UserDao;
import com.radioteria.db.entities.User;
import com.radioteria.support.services.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RemindPasswordServiceImpl implements RemindPasswordService {
    final private static Long PASSWORD_RECOVERY_CODE_TTL = TimeUnit.MINUTES.toMillis(1);

    private UserDao userDao;
    private EmailService emailService;
    private TemplateService templateService;
    private UserService userService;

    @Autowired
    public RemindPasswordServiceImpl(UserDao userDao, EmailService emailService,
                                     TemplateService templateService, UserService userService) {
        this.userDao = userDao;
        this.emailService = emailService;
        this.templateService = templateService;
        this.userService = userService;
    }

    @Override
    public void sendRemindPasswordLetter(User user) {
        if (user.getState() == UserState.DELETED) {
            throw new RemindPasswordServiceException("User is deleted.");
        }
        String passwordRecoveryCode = generateAndEncodePasswordRecoveryCode(user);
        Map<String, Object> context = new HashMap<String, Object>() {{
            this.put("user", user);
            this.put("code", passwordRecoveryCode);
        }};
        String body = templateService.render("email.remind-password", context);
        emailService.send(user.getEmail(), "Radioteria Password Recover", body);
    }

    private String generateAndEncodePasswordRecoveryCode(User user) {
        long time = System.currentTimeMillis() + PASSWORD_RECOVERY_CODE_TTL;
        PasswordRecoveryCode recoveryCode = new PasswordRecoveryCode(user, time);

        return recoveryCode.encode();
    }

    @Override
    public void changePasswordUsingRecoveryCode(String code, String newPassword) {
        try {
            PasswordRecoveryCode recoveryCode = decodeRecoveryCodeAndVerifyStaleTime(code);
            User user = findUserMatchingRecoveryCode(recoveryCode);

            verifyThatUserMatchesRecoveryCode(recoveryCode, user);

            userService.changePassword(user, newPassword);
        } catch (IllegalArgumentException e) {
            throw new RemindPasswordServiceException("Specified code broken.", e);
        }
    }

    @Override
    public void verifyPasswordRecoveryCode(String code) {
        try {
            PasswordRecoveryCode recoveryCode = decodeRecoveryCodeAndVerifyStaleTime(code);
            User user = findUserMatchingRecoveryCode(recoveryCode);

            verifyThatUserMatchesRecoveryCode(recoveryCode, user);
        } catch (IllegalArgumentException e) {
            throw new RemindPasswordServiceException("Specified code broken.", e);
        }
    }

    private User findUserMatchingRecoveryCode(PasswordRecoveryCode recoveryCode) {
        return userDao.findByEmail(recoveryCode.getUserEmail()).orElseThrow(() ->
                new RemindPasswordServiceException("Specified code belongs to user that does not exist."));
    }

    private void verifyThatUserMatchesRecoveryCode(PasswordRecoveryCode recoveryCode, User user) {
        if (user.getState() == UserState.DELETED) {
            throw new RemindPasswordServiceException("Specified code belongs to user that was deleted.");
        }
        if (!recoveryCode.isMatchUserDigest(user)) {
            throw new RemindPasswordServiceException("Code verification failed.");
        }
    }

    private PasswordRecoveryCode decodeRecoveryCodeAndVerifyStaleTime(String encodedCode) {
        PasswordRecoveryCode recoveryCode = PasswordRecoveryCode.decode(encodedCode);
        if (recoveryCode.getCodeStaleTime() < System.currentTimeMillis()) {
            throw new RemindPasswordServiceException("Specified code is stale.");
        }
        return recoveryCode;
    }
}
