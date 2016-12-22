package com.radioteria.business.services.auth.exceptions;

public class UserNotFoundAuthServiceException extends AuthServiceException {

    public UserNotFoundAuthServiceException() {
        super();
    }

    public UserNotFoundAuthServiceException(String message) {
        super(message);
    }

    public UserNotFoundAuthServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundAuthServiceException(Throwable cause) {
        super(cause);
    }

    protected UserNotFoundAuthServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
