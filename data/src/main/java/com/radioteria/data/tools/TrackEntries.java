package com.radioteria.data.tools;

import com.radioteria.data.entities.Track;

import java.util.AbstractList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.radioteria.util.FunctionalUtil.statefulFunction;

public class TrackEntries extends AbstractList<TrackEntries.TrackEntry> {

    static class TrackEntry {

        final public long offset;
        final public Track track;

        TrackEntry(long offset, Track track) {
            this.offset = offset;
            this.track = track;
        }

    }

    static class TrackWithPosition {

        final public long position;
        final public Track track;

        TrackWithPosition(long position, Track track) {
            this.position = position;
            this.track = track;
        }

    }

    private final List<TrackEntry> trackEntries;

    public TrackEntries(List<Track> tracks) {
        trackEntries = tracks.stream()
                .map(statefulFunction(0L, TrackEntry::new, (offset, track) -> track.getDuration() + offset))
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

    public Optional<TrackWithPosition> trackAtPlayingTime(long playingTime) {
        return find(trackEntry -> {
            long leftBound = trackEntry.offset;
            long rightBound = trackEntry.offset + trackEntry.track.getDuration();
            return leftBound <= playingTime && playingTime < rightBound;
        }).map(trackEntry -> new TrackWithPosition(playingTime - trackEntry.offset, trackEntry.track));
    }

    public Optional<TrackEntry> find(Predicate<TrackEntry> predicate) {
        return trackEntries.stream().filter(predicate).findFirst();
    }

}
