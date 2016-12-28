package com.radioteria.test.business.services.channelControls;

import com.radioteria.business.services.channels.impl.ChannelControlsServiceImpl;
import org.springframework.context.ApplicationEventPublisher;


public class ChannelControlServiceFixedTime extends ChannelControlsServiceImpl {

    private long currentTime;

    public ChannelControlServiceFixedTime(ApplicationEventPublisher eventPublisher, long fixedTime) {
        super(eventPublisher);
        this.currentTime = fixedTime;
    }

    @Override
    protected long getCurrentTimeMillis() {
        return currentTime;
    }
}
