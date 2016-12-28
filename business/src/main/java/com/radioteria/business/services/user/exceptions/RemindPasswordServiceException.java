package com.radioteria.business.services.user.exceptions;

import com.radioteria.business.services.exceptions.ServiceException;

public class RemindPasswordServiceException extends ServiceException {

    public RemindPasswordServiceException() {
        super();
    }

    public RemindPasswordServiceException(String message) {
        super(message);
    }

    public RemindPasswordServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemindPasswordServiceException(Throwable cause) {
        super(cause);
    }

    protected RemindPasswordServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
