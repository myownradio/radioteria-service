package com.radioteria.business.services.channels.api;

import com.radioteria.data.entities.Channel;

public interface ChannelControlsService {
    void start(Channel channel);
    void start(Channel channel, Long orderId);
    void stop(Channel channel);
    void next(Channel channel);
    void previous(Channel channel);
}
