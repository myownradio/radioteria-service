package com.radioteria.db.tests;

import com.radioteria.db.lists.ChannelPlaylist;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Supplier;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class PlaylistServiceTest {
    private ChannelPlaylist channelPlaylist;
    private ChannelPlaylist emptyChannelPlaylist;

    @Before
    public void setup() {
        Channel channel = new Channel() {{
            this.addTrack(new Track("Track 1", 125_000));
            this.addTrack(new Track("Track 2", 250_000));
            this.addTrack(new Track("Track 3", 900));
            this.addTrack(new Track("Track 4", 500_000));
        }};
        Channel emptyChannel = new Channel();

        channelPlaylist = new ChannelPlaylist(channel);
        emptyChannelPlaylist = new ChannelPlaylist(emptyChannel);
    }

    @Test
    public void notEmpty() {
        TestCase.assertFalse(channelPlaylist.isEmpty());
    }

    @Test
    public void empty() {
        assertTrue(emptyChannelPlaylist.isEmpty());
    }

    @Test
    public void getFirstTrackNotEmpty() {
        trackAtIndexEquals(0, () -> channelPlaylist.getFirstTrack());
    }

    @Test
    public void getFirstTrackEmpty() {
        Optional<Track> firstTrack = emptyChannelPlaylist.getFirstTrack();

        assertFalse(firstTrack.isPresent());
    }

    @Test
    public void getLastTrackNotEmpty() {
        trackAtIndexEquals(3, () -> channelPlaylist.getLastTrack());
    }

    @Test
    public void getLastTrackEmpty() {
        Optional<Track> firstTrack = emptyChannelPlaylist.getFirstTrack();

        assertFalse(firstTrack.isPresent());
    }

    @Test
    public void getTrackAtCursorNotEmpty() {
        trackAtIndexEquals(0, () -> channelPlaylist.getTrackAtCursor(0));
        trackAtIndexEquals(0, () -> channelPlaylist.getTrackAtCursor(64_000));
        trackAtIndexEquals(0, () -> channelPlaylist.getTrackAtCursor(124_999));

        trackAtIndexEquals(1, () -> channelPlaylist.getTrackAtCursor(125_000));
        trackAtIndexEquals(1, () -> channelPlaylist.getTrackAtCursor(250_000));
        trackAtIndexEquals(1, () -> channelPlaylist.getTrackAtCursor(374_999));

        trackAtIndexEquals(2, () -> channelPlaylist.getTrackAtCursor(375_000));
        trackAtIndexEquals(2, () -> channelPlaylist.getTrackAtCursor(375_899));

        trackAtIndexEquals(3, () -> channelPlaylist.getTrackAtCursor(375_900));
        trackAtIndexEquals(3, () -> channelPlaylist.getTrackAtCursor(500_000));
        trackAtIndexEquals(3, () -> channelPlaylist.getTrackAtCursor(875_899));

        assertEquals(Optional.empty(), channelPlaylist.getTrackAtCursor(875_900));
    }

    @Test
    public void getTrackAtCursorEmpty() {
        assertEquals(Optional.empty(), emptyChannelPlaylist.getTrackAtCursor(0));
        assertEquals(Optional.empty(), emptyChannelPlaylist.getTrackAtCursor(1_000));
        assertEquals(Optional.empty(), emptyChannelPlaylist.getTrackAtCursor(1_000_000));
    }

    @Test
    public void getTrackAfterNotEmpty() {
//        trackAtIndexEquals(1, () -> channelPlaylist.getTrackAfter(channelPlaylist.get(0).get()));
//        trackAtIndexEquals(2, () -> channelPlaylist.getTrackAfter(channelPlaylist.get(1).get()));
//        trackAtIndexEquals(3, () -> channelPlaylist.getTrackAfter(channelPlaylist.get(2).get()));
//        trackAtIndexEquals(0, () -> channelPlaylist.getTrackAfter(channelPlaylist.get(3).get()));
    }

    @Test
    public void getTrackAfterEmpty() {
//        assertEquals(Optional.empty(), emptyChannelPlaylist.getTrackAfter(new Track("Any Track", 1000)));
    }

    private void trackAtIndexEquals(int index, Supplier<Optional<Track>> trackSupplier) {
        assertEquals(
                channelPlaylist.get(index).map(Track::getTitle),
                trackSupplier.get().map(Track::getTitle)
        );
    }

}
