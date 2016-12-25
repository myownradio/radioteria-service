package com.radioteria.backing.template;

public class TemplateServiceException extends RuntimeException {

    public TemplateServiceException() {
        super();
    }

    public TemplateServiceException(String message) {
        super(message);
    }

    public TemplateServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateServiceException(Throwable cause) {
        super(cause);
    }

    protected TemplateServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
