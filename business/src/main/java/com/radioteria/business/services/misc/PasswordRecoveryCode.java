package com.radioteria.business.services.misc;

import org.springframework.util.DigestUtils;

import java.util.Base64;

public class PasswordRecoveryCode {

    final public static String CODE_DELIMITER = ":";

    private long codeStaleTime;
    private String userEmail;
    private String userDigest;

    public PasswordRecoveryCode(User user, long codeStaleTime) {
        this(user.getEmail(), generateUserDigest(user), codeStaleTime);
    }

    public PasswordRecoveryCode(String userEmail, String userDigest, long codeStaleTime) {
        this.userEmail = userEmail;
        this.userDigest = userDigest;
        this.codeStaleTime = codeStaleTime;
    }

    public long getCodeStaleTime() {
        return codeStaleTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserDigest() {
        return userDigest;
    }

    public String encode() {
        Base64.Encoder encoder = Base64.getEncoder();
        CharSequence[] codeParts = new String[] { userEmail, userDigest, Long.toString(codeStaleTime) };
        return encoder.encodeToString(String.join(CODE_DELIMITER, codeParts).getBytes());
    }

    public static PasswordRecoveryCode decode(String encodedCode) {
        String decodedCode = decodePasswordRecoveryCode(encodedCode);
        String[] codeParts = decodedCode.split(CODE_DELIMITER, 3);

        if (codeParts.length != 3) {
            throw new IllegalArgumentException("Specified code is not valid.");
        }

        String userEmail = codeParts[0];
        String userDigest = codeParts[1];
        long codeStaleTime = Long.parseLong(codeParts[2]);

        return new PasswordRecoveryCode(userEmail, userDigest, codeStaleTime);
    }

    public boolean digestMatches(User user) {
        return userDigest.equals(generateUserDigest(user));
    }

    private static String decodePasswordRecoveryCode(String code) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(code));
    }

    private static String generateUserDigest(User user) {
        String userCredentials = user.getEmail() + user.getPassword();
        return DigestUtils.md5DigestAsHex(userCredentials.getBytes());
    }
}
