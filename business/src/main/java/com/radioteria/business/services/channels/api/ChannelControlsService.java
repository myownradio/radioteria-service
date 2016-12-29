package com.radioteria.business.services.channels.api;

import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;
import com.radioteria.util.Tuple;

public interface ChannelControlsService {
    void start(Channel channel);
    void start(Channel channel, long orderId);
    void stop(Channel channel);
    void next(Channel channel);
    void previous(Channel channel);
    void forward(Channel channel, long millis);
    void backward(Channel channel, long millis);
    void exactly(Channel channel, long millis);
    Tuple<Track, Long> nowPlaying(Channel channel);
}
