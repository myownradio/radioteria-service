package com.radioteria.db.enumerations;

import com.radioteria.db.entities.Channel;

public enum ChannelState {
    STOPPED, STREAMING;

    public static ChannelState of(Channel channel) {
        return channel.getStartedAt() == null ? STOPPED : STREAMING;
    }
}
