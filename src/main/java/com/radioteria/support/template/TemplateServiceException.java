package com.radioteria.support.template;

import com.radioteria.support.exceptions.BackingServiceException;

public class TemplateServiceException extends BackingServiceException {
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
