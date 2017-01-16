package com.radioteria.business.services.channels.impl;

import com.radioteria.business.events.channelControl.ChannelControlsEvent;
import com.radioteria.business.services.channels.api.ChannelControlsService;
import com.radioteria.business.services.channels.exceptions.ChannelControlsServiceException;
import com.radioteria.db.enums.ChannelState;
import com.radioteria.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;


@Service
public class ChannelControlsServiceImpl implements ChannelControlsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChannelControlsServiceImpl.class);

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ChannelControlsServiceImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void start(Channel channel) {
        start(channel, 1L);
    }

    @Override
    public void start(Channel channel, long orderId) {

//        if (channel.getTracksDuration() == 0L) {
//            LOGGER.warn("Tried to start an empty channel {}.", channel.getId());
//            return;
//        }

        LOGGER.info("Starting channel {} from track {} position.", channel.getId(), orderId);

//        Long trackOffset = channel.getTrackOffsetByOrderId(orderId);

//        channel.setStartedAt(getCurrentTimeMillis() - trackOffset);

        publishChannelControlsEvent(channel);

    }

    @Override
    public void stop(Channel channel) {

        LOGGER.info("Stopping channel {}.", channel.getId());

        channel.setStartedAt(null);

        publishChannelControlsEvent(channel);

    }

    @Override
    public void next(Channel channel) {
//        playBasedOnCurrent(channel, channel::getTrackAfter);
    }

    @Override
    public void previous(Channel channel) {
//        playBasedOnCurrent(channel, channel::getTrackBefore);
    }

    private void playBasedOnCurrent(Channel channel, Function<Track, Optional<Track>> nextTrackGetter) {
        long time = getCurrentTimeMillis();
        Track nextTrack = channel.getPlayingAt(time)
                .flatMap(nextTrackGetter)
                .orElseThrow(() -> new ChannelControlsServiceException(
                        String.format("Nothing to play next on channel %d.", channel.getId())
                ));

        start(channel, nextTrack.getOrderId());
    }

    @Override
    public void forward(Channel channel, long millis) {
        if (channel.getChannelState() == ChannelState.STOPPED) {
            throw new ChannelControlsServiceException(
                    String.format("Could not forward stopped channel %d.", channel.getId())
            );
        }
        channel.setStartedAt(channel.getStartedAt() - millis);

        publishChannelControlsEvent(channel);
    }

    @Override
    public void backward(Channel channel, long millis) {
        if (channel.getChannelState() == ChannelState.STOPPED) {
            throw new ChannelControlsServiceException(
                    String.format("Could not backward stopped channel %d.", channel.getId())
            );
        }
        channel.setStartedAt(channel.getStartedAt() + millis);

        publishChannelControlsEvent(channel);
    }

    @Override
    public void exactly(Channel channel, long millis) {

        channel.setStartedAt(getCurrentTimeMillis() - millis);

        publishChannelControlsEvent(channel);

    }

    @Override
    public Tuple<Track, Long> nowPlaying(Channel channel) {
        return channel.getShortPlayingPositionAt(getCurrentTimeMillis())
                .flatMap(channel::getTrackWithPositionAtTimePosition)
                .orElseThrow(() -> new ChannelControlsServiceException(
                        String.format("Channel %d is stopped.", channel.getId())));
    }

    private void publishChannelControlsEvent(Channel channel) {

        LOGGER.info("Publishing channel controls event for channel {}.", channel.getId());

        ApplicationEvent event = new ChannelControlsEvent(this, channel);

        eventPublisher.publishEvent(event);

    }

    protected long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

}
