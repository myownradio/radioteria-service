package com.radioteria.business.services.impl;


import com.radioteria.business.services.api.RemindPasswordService;
import com.radioteria.business.services.exceptions.RemindPasswordException;
import com.radioteria.data.dao.api.UserDao;
import com.radioteria.data.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Base64;

@Service
public class RemindPasswordServiceImpl implements RemindPasswordService {

    final private static String CODE_DELIMITER = ":";


    @Value("remind.password.code.ttl")
    private Long remindCodeTtl;

    private UserDao userDao;

    @Autowired
    public RemindPasswordServiceImpl(UserDao userDao) {

        this.userDao = userDao;

    }

    public void sendRemindPasswordLetter(User user) {

        String passwordRecoveryCode = generatePasswordRecoveryCode(user);

        // sendEmail

    }

    public void verifyRemindPasswordCode(String code) {

        String decodedCode = decodePasswordRecoveryCode(code);

        String[] codeParts = decodedCode.split(CODE_DELIMITER, 3);

        if (codeParts.length != 3) {
            throw new RemindPasswordException("Specified code is invalid.");
        }

        long codeStaleTime = Long.parseLong(codeParts[0]);
        String userEmail = codeParts[1];
        String userDigest = codeParts[2];

        if (codeStaleTime > System.currentTimeMillis()) {
            throw new RemindPasswordException("Specified code is stale.");
        }

        User user = userDao.findByEmail(userEmail);

        if (user == null) {
            throw new RemindPasswordException("Specified code belongs to user that does not exist.");
        }

        if (!matchUserDigest(user, userDigest)) {
            throw new RemindPasswordException("Code verification failed.");
        }

    }

    private String decodePasswordRecoveryCode(String code) {

        Base64.Decoder decoder = Base64.getDecoder();

        try {
            return new String(decoder.decode(code));
        } catch (IllegalArgumentException e) {
            throw new RemindPasswordException("Specified code is broken.");
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

    private String generateUserDigest(User user) {

        String userCredentials = user.getEmail() + user.getPassword();

        return DigestUtils.md5DigestAsHex(userCredentials.getBytes());

    }

    private boolean matchUserDigest(User user, String digest) {

        return digest.equals(generateUserDigest(user));

    }

}
