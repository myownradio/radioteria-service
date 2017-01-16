package com.radioteria.business.events.channelControl;

import org.springframework.context.ApplicationEvent;

public class ChannelControlsEvent extends ApplicationEvent {

    private Channel channel;

    public ChannelControlsEvent(Object source, Channel channel) {
        super(source);
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

}
