package com.radioteria.db.dao.api;

import com.radioteria.db.entities.Channel;
import com.radioteria.db.entities.Track;

import java.util.List;
import java.util.Optional;

public interface TrackDao extends AbstractDao<Long, Track> {
    List<Track> findByChannel(Channel channel);
    List<Track> findByChannelId(Long channelId);
    Optional<Track> findByChannelAndOrderId(Channel channel, Long orderId);
}
