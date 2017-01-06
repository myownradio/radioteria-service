package com.radioteria.business.services.user.impl;

import com.radioteria.db.enumerations.UserState;
import com.radioteria.support.template.TemplateService;
import com.radioteria.business.services.user.api.RemindPasswordService;
import com.radioteria.business.services.user.exceptions.RemindPasswordServiceException;
import com.radioteria.db.dao.api.UserDao;
import com.radioteria.db.entities.User;
import com.radioteria.support.services.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RemindPasswordServiceImpl implements RemindPasswordService {

    final private static String CODE_DELIMITER = ":";

    @Value("${remind.password.code.ttl}")
    private Long remindCodeTtl = TimeUnit.MINUTES.toMillis(1);

    private UserDao userDao;

    private EmailService emailService;

    private TemplateService templateService;

    @Autowired
    public RemindPasswordServiceImpl(UserDao userDao, EmailService emailService, TemplateService templateService) {
        this.userDao = userDao;
        this.emailService = emailService;
        this.templateService = templateService;
    }

    public void sendRemindPasswordLetter(User user) {
        if (user.getState() == UserState.DELETED) {
            throw new RemindPasswordServiceException("User is deleted.");
        }
        String passwordRecoveryCode = generatePasswordRecoveryCode(user);
        Map<String, Object> context = new HashMap<String, Object>() {{
            this.put("user", user);
            this.put("code", passwordRecoveryCode);
        }};
        String body = templateService.render("email.remind-password", context);
        emailService.send(user.getEmail(), "Remind password", body);
    }

    public void verifyPasswordRecoveryCode(String code) {
        String decodedCode = decodePasswordRecoveryCode(code);
        String[] codeParts = decodedCode.split(CODE_DELIMITER, 3);

        if (codeParts.length != 3) {
            throw new RemindPasswordServiceException("Specified code is not valid.");
        }

        long codeStaleTime = Long.parseLong(codeParts[0]);
        String userEmail = codeParts[1];
        String userDigest = codeParts[2];

        if (codeStaleTime > System.currentTimeMillis()) {
            throw new RemindPasswordServiceException("Specified code is stale.");
        }

        User user = userDao.findByEmail(userEmail).orElseThrow(() ->
                new RemindPasswordServiceException("Specified code belongs to user that does not exist."));

        if (user.getState() == UserState.DELETED) {
            throw new RemindPasswordServiceException("Specified code belongs to user that was deleted.");
        }

        if (!matchUserDigest(user, userDigest)) {
            throw new RemindPasswordServiceException("Code verification failed.");
        }
    }

    private String generatePasswordRecoveryCode(User user) {
        Long time = System.currentTimeMillis() + remindCodeTtl;
        String userEmail = user.getEmail();
        String digest = generateUserDigest(user);

        Base64.Encoder encoder = Base64.getEncoder();

        CharSequence[] codeParts = new String[] { time.toString(), userEmail, digest };

        return encoder.encodeToString(String.join(CODE_DELIMITER, codeParts).getBytes());
    }

    private String decodePasswordRecoveryCode(String code) {
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            return new String(decoder.decode(code));
        } catch (IllegalArgumentException e) {
            throw new RemindPasswordServiceException("Specified code is broken.");
        }
    }

    private String generateUserDigest(User user) {
        String userCredentials = user.getEmail() + user.getPassword();
        return DigestUtils.md5DigestAsHex(userCredentials.getBytes());
    }

    private boolean matchUserDigest(User user, String digest) {
        return digest.equals(generateUserDigest(user));
    }
}
