package com.radioteria.data.dao.api;

import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;

import java.util.List;

public interface TrackDao extends AbstractDao<Long, Track> {
    List<Track> findByChannel(Channel channel);
    List<Track> findByChannel(Long channelId);
}
