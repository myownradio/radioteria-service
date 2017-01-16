package com.radioteria.db.dao.impl;

import com.radioteria.db.dao.api.TrackDao;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class TrackDaoImpl extends AbstractDaoImpl<Long, Track> implements TrackDao {

    public TrackDaoImpl() {
        super(Long.class, Track.class);
    }

    @Override
    public List<Track> findByChannel(Channel channel) {
        return findByChannelId(channel.getId());
    }

    @Override
    public List<Track> findByChannelId(Long channelId) {
        return listByPropertyValue(Track.CHANNEL_ID, channelId);
    }

    @Override
    public Optional<Track> findByChannelAndOrderId(Channel channel, Long orderId) {

        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Track> criteriaQuery = criteriaBuilder.createQuery(Track.class);
        Root<Track> trackRoot = criteriaQuery.from(Track.class);

        criteriaQuery.select(trackRoot);
        criteriaQuery.where(criteriaBuilder.and(
                criteriaBuilder.equal(trackRoot.get(Track.CHANNEL_ID), channel.getId()),
                criteriaBuilder.equal(trackRoot.get(Track.ORDER_ID), orderId)
        ));

        Track track = getEntityManager().createQuery(criteriaQuery).getSingleResult();

        return Optional.ofNullable(track);

    }
}
