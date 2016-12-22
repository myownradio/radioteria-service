package com.radioteria.business.services.auth.exceptions;

import com.radioteria.business.services.ServiceException;

public class AuthServiceException extends ServiceException {

    public AuthServiceException() {
        super();
    }

    public AuthServiceException(String message) {
        super(message);
    }

    public AuthServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthServiceException(Throwable cause) {
        super(cause);
    }

    protected AuthServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
