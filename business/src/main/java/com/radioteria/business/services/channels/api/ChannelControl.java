package com.radioteria.business.services.channels.api;

import com.radioteria.backing.util.Tuple;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;

public interface ChannelControl {
    void start(Channel channel);
    void startFrom(Track track, Channel channel);
    void stop(Channel channel);
    void next(Channel channel);
    void previous(Channel channel);
    Tuple<Track, Long> now(Channel channel);
}
