package com.radioteria.business.services.auth.exceptions;

public class UserExistsException extends AuthServiceException {

    public UserExistsException() {
        super();
    }

    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExistsException(Throwable cause) {
        super(cause);
    }

    protected UserExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
