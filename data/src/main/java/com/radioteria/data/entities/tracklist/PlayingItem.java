package com.radioteria.data.entities.tracklist;

import com.radioteria.data.entities.Track;

public class PlayingItem {

    private final Track track;
    private final long timePosition;

    PlayingItem(Track track, long timePosition) {
        this.track = track;
        this.timePosition = timePosition;
    }

    public Track getTrack() {
        return track;
    }

    public long getTimePosition() {
        return timePosition;
    }

}
