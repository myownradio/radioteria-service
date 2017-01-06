package com.radioteria.business.services.user.exceptions;

import com.radioteria.business.services.exceptions.ServiceException;

public class PasswordRecoveryServiceException extends ServiceException {

    public PasswordRecoveryServiceException() {
        super();
    }

    public PasswordRecoveryServiceException(String message) {
        super(message);
    }

    public PasswordRecoveryServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordRecoveryServiceException(Throwable cause) {
        super(cause);
    }

    protected PasswordRecoveryServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
