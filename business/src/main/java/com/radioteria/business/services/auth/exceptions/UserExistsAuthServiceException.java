package com.radioteria.business.services.auth.exceptions;

public class UserExistsAuthServiceException extends AuthServiceException {

    public UserExistsAuthServiceException() {
        super();
    }

    public UserExistsAuthServiceException(String message) {
        super(message);
    }

    public UserExistsAuthServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExistsAuthServiceException(Throwable cause) {
        super(cause);
    }

    protected UserExistsAuthServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
