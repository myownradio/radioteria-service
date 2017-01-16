package com.radioteria.business.services.user.impl;

import com.radioteria.business.services.user.api.PlaylistService;
import one.util.streamex.StreamEx;

import java.util.List;

public class PlaylistServiceImpl implements PlaylistService {
    final private List<Track> tracks;

    public PlaylistServiceImpl(List<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public List<Track> getTracks() {
        return tracks;
    }

    @Override
    public boolean isEmpty() {
        return getTracksDuration() == 0L;
    }

    public Long getTracksDuration() {
        return getTracksStream()
                .mapToLong(Track::getDuration).sum();
    }

//    public Optional<Long> getCursorPositionAtTime(long timeMillis) {
//        long tracksDuration = getTracksDuration();
//
//        if (isEmpty() || getChannel().isStopped()) {
//            return Optional.empty();
//        }
//
//        return Optional.of(timeMillis - getChannel().getStartedAt() % tracksDuration);
//    }

    public Long getTrackOffsetByOrderId(Long orderId) {
        return getTracksStream()
                .filter(t -> t.getOrderId() < orderId)
                .mapToLong(Track::getDuration)
                .sum();
    }

    private StreamEx<Track> getTracksStream() {
        return StreamEx.of(getTracks());
    }
}
