package com.radioteria.business.services.channels.exceptions;

import com.radioteria.business.services.exceptions.ServiceException;

public class ChannelControlsServiceException extends ServiceException {

    public ChannelControlsServiceException() {
        super();
    }

    public ChannelControlsServiceException(String message) {
        super(message);
    }

    public ChannelControlsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelControlsServiceException(Throwable cause) {
        super(cause);
    }

    protected ChannelControlsServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
