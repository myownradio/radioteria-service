package com.radioteria.test.business.services.channelControls;

import com.radioteria.business.services.channels.api.ChannelControlsService;
import com.radioteria.business.services.channels.impl.ChannelControlsServiceImpl;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;
import com.radioteria.data.entities.User;
import com.radioteria.data.enumerations.ChannelState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class ChannelControlsTest {

    final private static Long TIME = 100L;

    private User user;

    private Channel channel;

    private ChannelControlsService channelControlsService;

    private class ChannelControlsServiceWithFixedTime extends ChannelControlsServiceImpl {
        ChannelControlsServiceWithFixedTime(ApplicationEventPublisher eventPublisher) {
            super(eventPublisher);
        }

        @Override
        protected long getCurrentTimeMillis() {
            return TIME;
        }
    }

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Before
    public void setup() {

        user = new User();

        channel = new Channel();
        channel.setChannelState(ChannelState.STOPPED);

        user.addChannel(channel);

        channel.setName("Test Channel #1");
        channel.addTrack(new Track("Test Track #1", 10L));
        channel.addTrack(new Track("Test Track #2", 500L));
        channel.addTrack(new Track("Test Track #3", 1500L));
        channel.addTrack(new Track("Test Track #4", 250L));
        channel.addTrack(new Track("Test Track #5", 500L));

        channelControlsService = new ChannelControlsServiceWithFixedTime(eventPublisher);

    }

    @Test
    public void testChannelStart() {
        channelControlsService.start(channel);

        assertEquals(ChannelState.STREAMING, channel.getChannelState());

        Optional<Track> nowPlaying = channel.getPlayingAt(TIME);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #1", nowPlaying.get().getTitle());
    }

    @Test
    public void testChannelStartFrom() {
        channelControlsService.startFrom(2L, channel);

        assertEquals(ChannelState.STREAMING, channel.getChannelState());

        Optional<Track> nowPlaying = channel.getPlayingAt(TIME);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #2", nowPlaying.get().getTitle());
    }

    @Test
    public void testChannelStop() {
        channelControlsService.start(channel);

        assertEquals(ChannelState.STREAMING, channel.getChannelState());

        channelControlsService.stop(channel);

        assertEquals(ChannelState.STOPPED, channel.getChannelState());
        assertFalse(channel.getPlayingAt(TIME).isPresent());
    }

    @Test
    public void testChannelPlayNext() {
        channelControlsService.startFrom(3L, channel);
        channelControlsService.next(channel);

        Optional<Track> nowPlaying = channel.getPlayingAt(TIME);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #4", nowPlaying.get().getTitle());
    }


    @Test
    public void testChannelPlayPrevious() {
        channelControlsService.startFrom(3L, channel);
        channelControlsService.previous(channel);

        Optional<Track> nowPlaying = channel.getPlayingAt(100L);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #2", nowPlaying.get().getTitle());
    }

    @Test
    public void testChannelPlayNextIfLast() {
        channelControlsService.startFrom(5L, channel);
        channelControlsService.next(channel);

        Optional<Track> nowPlaying = channel.getPlayingAt(100L);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #1", nowPlaying.get().getTitle());
    }

    @Test
    public void testChannelPlayPreviousIfFirst() {
        channelControlsService.startFrom(1L, channel);
        channelControlsService.previous(channel);

        Optional<Track> nowPlaying = channel.getPlayingAt(100L);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #5", nowPlaying.get().getTitle());
    }

}
