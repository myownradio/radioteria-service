package com.radioteria.backing.exceptions;

public class BackingServiceException extends Exception {

    public BackingServiceException() {
    }

    public BackingServiceException(String message) {
        super(message);
    }

    public BackingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BackingServiceException(Throwable cause) {
        super(cause);
    }

    public BackingServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
