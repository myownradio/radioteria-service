package com.radioteria.business.services.channels.exceptions;

import com.radioteria.business.services.exceptions.ServiceException;

public class ChannelControlException extends ServiceException {

    public ChannelControlException() {
        super();
    }

    public ChannelControlException(String message) {
        super(message);
    }

    public ChannelControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelControlException(Throwable cause) {
        super(cause);
    }

    protected ChannelControlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
