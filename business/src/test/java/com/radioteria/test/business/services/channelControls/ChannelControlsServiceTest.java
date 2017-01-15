package com.radioteria.test.business.services.channelControls;

import com.radioteria.business.events.channelControl.ChannelControlsEvent;
import com.radioteria.business.services.channels.api.ChannelControlsService;
import com.radioteria.business.services.channels.exceptions.ChannelControlsServiceException;
import com.radioteria.business.services.channels.impl.ChannelControlsServiceImpl;
import com.radioteria.db.entities.Channel;
import com.radioteria.db.entities.Track;
import com.radioteria.db.entities.User;
import com.radioteria.db.enumerations.ChannelState;
import com.radioteria.util.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ChannelControlsServiceTest {

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
        channel.setId(1L);

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

        verifyThatEventPublishedTimes(times(1));

    }

    @Test
    public void testChannelStartFrom() {

        channelControlsService.start(channel, 2L);

        assertEquals(ChannelState.STREAMING, channel.getChannelState());

        Optional<Track> nowPlaying = channel.getPlayingAt(TIME);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #2", nowPlaying.get().getTitle());

        verifyThatEventPublishedTimes(times(1));

    }

    @Test
    public void testChannelStop() {

        channelControlsService.start(channel);

        assertEquals(ChannelState.STREAMING, channel.getChannelState());

        channelControlsService.stop(channel);

        assertEquals(ChannelState.STOPPED, channel.getChannelState());
        assertFalse(channel.getPlayingAt(TIME).isPresent());

        verifyThatEventPublishedTimes(times(2));

    }

    @Test
    public void testChannelPlayNext() {

        channelControlsService.start(channel, 3L);
        channelControlsService.next(channel);

        Optional<Track> nowPlaying = channel.getPlayingAt(TIME);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #4", nowPlaying.get().getTitle());

        verifyThatEventPublishedTimes(times(2));

    }


    @Test
    public void testChannelPlayPrevious() {

        channelControlsService.start(channel, 3L);
        channelControlsService.previous(channel);

        Optional<Track> nowPlaying = channel.getPlayingAt(100L);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #2", nowPlaying.get().getTitle());

        verifyThatEventPublishedTimes(times(2));

    }

    @Test
    public void testChannelPlayNextIfLast() {

        channelControlsService.start(channel, 5L);
        channelControlsService.next(channel);

        Optional<Track> nowPlaying = channel.getPlayingAt(100L);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #1", nowPlaying.get().getTitle());

        verifyThatEventPublishedTimes(times(2));

    }

    @Test
    public void testChannelPlayPreviousIfFirst() {

        channelControlsService.start(channel, 1L);
        channelControlsService.previous(channel);

        Optional<Track> nowPlaying = channel.getPlayingAt(100L);

        assertTrue(nowPlaying.isPresent());
        assertEquals("Test Track #5", nowPlaying.get().getTitle());

        verifyThatEventPublishedTimes(times(2));

    }

    @Test
    public void testStartEmptyChannel() {

        channel.getTracks().clear();

        channelControlsService.start(channel);

        assertEquals(ChannelState.STOPPED, channel.getChannelState());

        verifyThatEventPublishedTimes(never());

    }

    @Test
    public void testStartWithZeroLength() {

        channel.getTracks().clear();
        channel.addTrack(new Track("Broken Zero", 0));
        channel.addTrack(new Track("Another Broken Zero", 0));

        channelControlsService.start(channel);

        assertEquals(ChannelState.STOPPED, channel.getChannelState());

        verifyThatEventPublishedTimes(never());

    }

    @Test
    public void testStartFromWrongOrder() {

        channelControlsService.start(channel, 10L);
        verifyThatPlayingFirstTrack();

        channelControlsService.start(channel, -10L);
        verifyThatPlayingFirstTrack();

    }

    @Test(expected = ChannelControlsServiceException.class)
    public void testPlayNextOnStoppedChannel() {
        channelControlsService.next(channel);
    }

    @Test
    public void testCurrentTimeMillis() {

        long time = System.currentTimeMillis();

        ChannelControlsService channelControlsService = new ChannelControlsServiceImpl(eventPublisher);
        channelControlsService.start(channel);

        assertTrue(channel.getStartedAt() >= time);

    }

    @Test
    public void testExactly() {

        channelControlsService.exactly(channel, 511L);

        Tuple<Track, Long> nowPlaying = channelControlsService.nowPlaying(channel);

        assertEquals("Test Track #3", nowPlaying.X.getTitle());
        assertEquals(new Long(1L), nowPlaying.Y);

        verifyThatEventPublishedTimes(times(1));

    }

    @Test
    public void testForward() {

        channelControlsService.exactly(channel, 511L);
        channelControlsService.forward(channel, 1500L);

        Tuple<Track, Long> nowPlaying = channelControlsService.nowPlaying(channel);

        assertEquals("Test Track #4", nowPlaying.X.getTitle());
        assertEquals(new Long(1L), nowPlaying.Y);

        verifyThatEventPublishedTimes(times(2));

    }

    @Test
    public void testBackward() {

        channelControlsService.exactly(channel, 511L);
        channelControlsService.backward(channel, 500L);

        Tuple<Track, Long> nowPlaying = channelControlsService.nowPlaying(channel);

        assertEquals("Test Track #2", nowPlaying.X.getTitle());
        assertEquals(new Long(1L), nowPlaying.Y);

        verifyThatEventPublishedTimes(times(2));

    }

    @Test(expected = ChannelControlsServiceException.class)
    public void testForwardWhenStopped() {

        channelControlsService.forward(channel, 1500L);

    }

    @Test(expected = ChannelControlsServiceException.class)
    public void testBackwardWhenStopped() {

        channelControlsService.backward(channel, 1500L);

    }

    private void verifyThatPlayingFirstTrack() {
        Optional<Track> track = channel.getPlayingAt(TIME);
        assertEquals(Optional.of("Test Track #1"), track.map(Track::getTitle));
    }

    private void verifyThatEventPublishedTimes(VerificationMode mode) {
        verify(eventPublisher, mode).publishEvent(isA(ChannelControlsEvent.class));
    }

}
