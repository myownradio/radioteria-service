package com.radioteria.business.events.channelControl;

import com.radioteria.data.entities.Channel;
import org.springframework.context.ApplicationEvent;

public class ChannelStateChangedEvent extends ApplicationEvent {

    private Channel channel;

    public ChannelStateChangedEvent(Object source, Channel channel) {
        super(source);
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

}
