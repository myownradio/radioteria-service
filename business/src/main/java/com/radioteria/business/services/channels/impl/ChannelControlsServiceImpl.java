package com.radioteria.business.services.channels.impl;

import com.radioteria.business.events.channelControl.ChannelControlsEvent;
import com.radioteria.business.services.channels.api.ChannelControlsService;
import com.radioteria.business.services.channels.exceptions.ChannelControlsServiceException;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

import static com.radioteria.data.enumerations.ChannelState.STOPPED;
import static com.radioteria.data.enumerations.ChannelState.STREAMING;


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
    public void start(Channel channel, Long orderId) {

        if (channel.getTracksDuration() == 0L) {
            LOGGER.warn("Tried to start an empty channel {}.", channel.getId());
            return;
        }

        LOGGER.info("Starting channel {} from track {} position.", channel.getId(), orderId);

        Long trackOffset = channel.getTrackOffsetByOrderId(orderId);

        channel.setChannelState(STREAMING);
        channel.setStartedAt(getCurrentTimeMillis() - trackOffset);

        publishChannelControlsEvent(channel);

    }

    @Override
    public void stop(Channel channel) {

        LOGGER.info("Stopping channel {}.", channel.getId());

        channel.setChannelState(STOPPED);
        channel.setStartedAt(null);

        publishChannelControlsEvent(channel);

    }

    @Override
    public void next(Channel channel) {
        playBasedOnCurrent(channel, channel::getTrackAfter);
    }

    @Override
    public void previous(Channel channel) {
        playBasedOnCurrent(channel, channel::getTrackBefore);
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

    private void publishChannelControlsEvent(Channel channel) {

        LOGGER.info("Publishing channel controls event for channel {}.", channel.getId());

        ApplicationEvent event = new ChannelControlsEvent(this, channel);

        eventPublisher.publishEvent(event);

    }

    protected long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

}
