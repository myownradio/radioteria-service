package com.radioteria.business.services.channels.impl;

import com.radioteria.backing.util.Tuple;
import com.radioteria.business.events.channelControl.ChannelPlaybackUpdatedEvent;
import com.radioteria.business.services.channels.api.ChannelControlsService;
import com.radioteria.business.services.channels.exceptions.ChannelControlsServiceException;
import com.radioteria.data.dao.api.TrackDao;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;
import com.radioteria.data.enumerations.ChannelState;
import com.radioteria.util.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.radioteria.data.enumerations.ChannelState.STOPPED;


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

        LOGGER.info("Starting channel (channel={}).", channel.getId());

        modifyChannel(channel, ch -> {
            ch.setChannelState(ChannelState.STREAMING);
            ch.setStartedAt(System.currentTimeMillis());
        });

    }

    @Override
    public void startFrom(Long orderId, Channel channel) {

        LOGGER.info("Starting channel {} from track {} position.", channel.getId(), orderId);

        modifyChannel(channel, ch -> {
            Long trackOffset = ch.getTrackOffsetAtOrderId(orderId);
            ch.setStartedAt(System.currentTimeMillis() - trackOffset);
        });

    }

    @Override
    public void stop(Channel channel) {

        modifyChannel(channel, ch -> {
            ch.setChannelState(STOPPED);
            ch.setStartedAt(null);
        });

    }

    @Override
    public void next(Channel channel) {
        playBasedOnCurrent(channel, channel::getTrackBefore);
    }

    @Override
    public void previous(Channel channel) {
        playBasedOnCurrent(channel, channel::getTrackAfter);
    }

    private void playBasedOnCurrent(Channel channel, Function<Track, Optional<Track>> nextTrackGetter) {
        long time = System.currentTimeMillis();
        Track nextTrack = channel.getPlayingAt(time)
                .flatMap(nextTrackGetter)
                .orElseThrow(() -> new ChannelControlsServiceException(
                        String.format("Nothing to play next on channel %d.", channel.getId())
                ));

        startFrom(nextTrack.getOrderId(), channel);
    }

    private void modifyChannel(Channel channel, Consumer<Channel> consumer) {

        LOGGER.info("Modifying channel {}.", channel.getId());
        consumer.accept(channel);

        LOGGER.info("Publishing event about channel {} playback update.", channel.getId());
        publishEventAboutChannelPlaybackUpdated(channel);

    }

    private void publishEventAboutChannelPlaybackUpdated(Channel channel) {

        ApplicationEvent event = new ChannelPlaybackUpdatedEvent(this, channel);

        eventPublisher.publishEvent(event);

    }

}
