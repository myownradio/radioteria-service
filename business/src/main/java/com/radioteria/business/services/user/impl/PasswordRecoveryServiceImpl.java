package com.radioteria.business.services.user.impl;

import com.radioteria.business.services.misc.PasswordRecoveryCode;
import com.radioteria.business.services.user.api.UserService;
import com.radioteria.support.template.TemplateService;
import com.radioteria.business.services.user.api.PasswordRecoveryService;
import com.radioteria.business.services.user.exceptions.PasswordRecoveryServiceException;
import com.radioteria.db.dao.api.UserDao;
import com.radioteria.support.services.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

    final public static String PASSWORD_RECOVERY_LETTER_SUBJECT = "Radioteria Password Recovery";
    final public static String PASSWORD_RECOVERY_LETTER_TEMPLATE = "email.password-recovery";
    final public static Long PASSWORD_RECOVERY_CODE_TTL = TimeUnit.MINUTES.toMillis(30);

    final private UserDao userDao;
    final private EmailService emailService;
    final private TemplateService templateService;
    final private UserService userService;

    @Autowired
    public PasswordRecoveryServiceImpl(UserDao userDao, EmailService emailService,
                                       TemplateService templateService, UserService userService) {
        this.userDao = userDao;
        this.emailService = emailService;
        this.templateService = templateService;
        this.userService = userService;
    }

    @Override
    public void sendPasswordRecoveryLetter(User user) {
        if (user.isDeleted()) {
            throw new PasswordRecoveryServiceException("User is deleted.");
        }

        String recoveryCode = getEncodedRecoveryCode(user);
        String body = renderRecoveryLetter(user, recoveryCode);

        sendLetterToUser(user, body);
    }

    private String getEncodedRecoveryCode(User user) {
        long codeStaleTime = makeRecoveryCodeStaleTime();
        PasswordRecoveryCode recoveryCode = new PasswordRecoveryCode(user, codeStaleTime);

        return recoveryCode.encode();
    }

    private long makeRecoveryCodeStaleTime() {
        return System.currentTimeMillis() + PASSWORD_RECOVERY_CODE_TTL;
    }

    private String renderRecoveryLetter(final User user, final String recoveryCode) {
        Map<String, Object> context = new HashMap<String, Object>() {{
            this.put("user", user);
            this.put("code", recoveryCode);
        }};
        return templateService.render(PASSWORD_RECOVERY_LETTER_TEMPLATE, context);
    }

    private void sendLetterToUser(User user, String body) {
        emailService.send(user.getEmail(), PASSWORD_RECOVERY_LETTER_SUBJECT, body);
    }

    @Override
    public void verifyPasswordRecoveryCode(String code) {
        try {
            PasswordRecoveryCode recoveryCode = verifyAndDecodeRecoveryCode(code);
            User user = getUserFromRecoveryCode(recoveryCode);

            failIfCodeNotCorrespondToTheUser(recoveryCode, user);
        } catch (IllegalArgumentException e) {
            throw new PasswordRecoveryServiceException("Specified code broken.", e);
        }
    }

    @Override
    public void changePasswordUsingRecoveryCode(String code, String newPassword) {
        try {
            PasswordRecoveryCode recoveryCode = verifyAndDecodeRecoveryCode(code);
            User user = getUserFromRecoveryCode(recoveryCode);

            failIfCodeNotCorrespondToTheUser(recoveryCode, user);

            userService.changePassword(user, newPassword);
        } catch (IllegalArgumentException e) {
            throw new PasswordRecoveryServiceException("Specified code broken.", e);
        }
    }

    private User getUserFromRecoveryCode(PasswordRecoveryCode recoveryCode) {
        return userDao.findByEmail(recoveryCode.getUserEmail()).orElseThrow(() ->
                new PasswordRecoveryServiceException("Specified code belongs to user that does not exist."));
    }

    private void failIfCodeNotCorrespondToTheUser(PasswordRecoveryCode recoveryCode, User user) {
        if (user.isDeleted()) {
            throw new PasswordRecoveryServiceException("Specified code belongs to user that was deleted.");
        }
        if (!recoveryCode.digestMatches(user)) {
            throw new PasswordRecoveryServiceException("Code verification failed.");
        }
    }

    private PasswordRecoveryCode verifyAndDecodeRecoveryCode(String encodedCode) {
        PasswordRecoveryCode recoveryCode = PasswordRecoveryCode.decode(encodedCode);
        if (isRecoveryCodeStale(recoveryCode)) {
            throw new PasswordRecoveryServiceException("Specified code is stale.");
        }
        return recoveryCode;
    }

    private boolean isRecoveryCodeStale(PasswordRecoveryCode recoveryCode) {
        return recoveryCode.getCodeStaleTime() < System.currentTimeMillis();
    }
}
