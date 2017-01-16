package com.radioteria.db.entities.tracklist;

import java.util.AbstractList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Tracklist extends AbstractList<Tracklist.TrackEntry> {

    class TrackEntry {
        final public long offset;
        final public Track track;

        TrackEntry(long offset, Track track) {
            this.offset = offset;
            this.track = track;
        }
    }

    private final List<TrackEntry> trackEntries;

    public Tracklist(List<Track> tracks) {
        AtomicLong offset = new AtomicLong(0);
        trackEntries = tracks.stream()
                .map(t -> new TrackEntry(offset.get(), t))
                .peek(e -> offset.addAndGet(e.track.getDuration()))
                .collect(Collectors.toList());
    }

    @Override
    public TrackEntry get(int index) {
        return trackEntries.get(index);
    }

    @Override
    public int size() {
        return trackEntries.size();
    }

    public long duration() {
        if (size() == 0) {
            return 0L;
        }
        TrackEntry lastTrackEntry = get(size() - 1);
        return lastTrackEntry.offset + lastTrackEntry.track.getDuration();
    }

    public Optional<PlayingItem> playingItem(long playingTime) {
        return find(trackEntry -> {
            long leftBound = trackEntry.offset;
            long rightBound = trackEntry.offset + trackEntry.track.getDuration();
            return leftBound <= playingTime && playingTime < rightBound;
        }).map(trackEntry ->
                new PlayingItem(trackEntry.track, playingTime - trackEntry.offset));
    }

    public Optional<TrackEntry> find(Predicate<TrackEntry> predicate) {
        return trackEntries.stream().filter(predicate).findFirst();
    }

}
