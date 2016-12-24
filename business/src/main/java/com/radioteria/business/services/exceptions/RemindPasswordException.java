package com.radioteria.business.services.exceptions;

public class RemindPasswordException extends ServiceException {

    public RemindPasswordException() {
        super();
    }

    public RemindPasswordException(String message) {
        super(message);
    }

    public RemindPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemindPasswordException(Throwable cause) {
        super(cause);
    }

    protected RemindPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
