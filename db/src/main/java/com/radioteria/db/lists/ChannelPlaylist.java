package com.radioteria.db.lists;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class ChannelPlaylist {
//    private Channel channel;
//
//    public ChannelPlaylist(Channel channel) {
//        this.channel = channel;
//    }
//
//    public boolean isEmpty() {
//        return getTracksDuration() == 0L;
//    }
//
//    public Long getTracksDuration() {
//        return getTracksStream()
//                .mapToLong(Track::getDuration)
//                .sum();
//    }
//
//    public Optional<Track> getFirstTrack() {
//        return getTracksStream()
//                .findFirst();
//    }
//
//    public Optional<Track> getLastTrack() {
//        return getTracksStream()
//                .sorted(Comparator.reverseOrder())
//                .findFirst();
//    }
//
//    public Optional<Track> getTrackAtCursor(long cursor) {
//        final AtomicLong offset = new AtomicLong(0);
//        return getTracksStream()
//                .filter(track -> {
//                    long leftBound = offset.getAndAdd(track.getDuration());
//                    long rightBound = leftBound + track.getDuration();
//
//                    return leftBound <= cursor && rightBound > cursor;
//                })
//                .findFirst();
//    }
//
//    public Optional<Track> get(int index) {
//        return index >= 0 && index < channel.getTracks().size()
//                ? Optional.of(channel.getTracks().get(index))
//                : Optional.empty();
//    }
//
//    private Stream<Track> getTracksStream() {
//        return channel.getTracks().stream();
//    }

}
