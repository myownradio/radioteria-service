package com.radioteria.data.dao.impl;

import com.radioteria.data.dao.api.TrackDao;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TrackDaoImpl extends AbstractDaoImpl<Long, Track> implements TrackDao {

    public TrackDaoImpl() {
        super(Long.class, Track.class);
    }

    public List<Track> findByChannel(Channel channel) {
        return findByChannelId(channel.getId());
    }

    public List<Track> findByChannelId(Long channelId) {
        return listByPropertyValue(Track.CHANNEL_ID, channelId);
    }

}
