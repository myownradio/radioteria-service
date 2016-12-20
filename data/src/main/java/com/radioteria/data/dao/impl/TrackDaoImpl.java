package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.TrackDao;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrackDaoImpl extends AbstractDaoImpl<Long, Track> implements TrackDao {

    public TrackDaoImpl(Class<Long> idClass, Class<Track> entityClass) {
        super(idClass, entityClass);
    }

    public List<Track> findByChannel(Channel channel) {
        return findByChannel(channel.getId());
    }

    public List<Track> findByChannel(Long channelId) {
        return listByPropertyValue(Track.CHANNEL_ID, Long.toString(channelId));
    }

}
