package com.radioteria.business.services.channels.impl;

import com.radioteria.backing.util.MathUtil;
import com.radioteria.backing.util.Tuple;
import com.radioteria.business.events.channelControl.ChannelPlaybackUpdatedEvent;
import com.radioteria.business.services.channels.api.ChannelControl;
import com.radioteria.business.services.channels.exceptions.ChannelControlException;
import com.radioteria.data.entities.Channel;
import com.radioteria.data.entities.Track;
import com.radioteria.data.enumerations.ChannelState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

import static com.radioteria.data.enumerations.ChannelState.STOPPED;


@Service
public class ChannelControlImpl implements ChannelControl {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChannelControlImpl.class);

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ChannelControlImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void start(Channel channel) {

        LOGGER.info("Starting channel (channel={}).", channel.getId());

        modifyChannel(channel, ch -> {
            ch.setChannelState(ChannelState.STREAMING);
            ch.setStartedAt(System.currentTimeMillis());
        });

    }

    public void startFrom(Track track, Channel channel) {

        LOGGER.info("Starting channel {} from track {} position.", channel.getId(), track.getId());

        modifyChannel(channel, ch -> {
            Long trackOffset = 0L;
            for (Track tr : channel.getTracks()) {
                if (tr.equals(track)) {
                    LOGGER.info("Track {} found at offset {}.", tr.getId(), trackOffset);
                    ch.setStartedAt(System.currentTimeMillis() - trackOffset);
                    return;
                }
                trackOffset += tr.getDuration();
            }

            throw new ChannelControlException(
                    String.format("Track %d does not belong to the channel %d.",
                            track.getId(), ch.getId())
            );
        });

    }

    public void stop(Channel channel) {

        modifyChannel(channel, ch -> {
            ch.setChannelState(STOPPED);
            ch.setStartedAt(null);
        });

    }

    public void next(Channel channel) {

    }

    public void previous(Channel channel) {

    }

    public Tuple<Track, Long> now(Channel channel) {

        if (channel.getChannelState() == STOPPED) {
            return null;
        }

        if (channel.getTracks().size() == 0) {
            LOGGER.warn("Channel {} is streaming, but there is no tracks.", channel.getId());
            return null;
        }

        long duration = channel.getTracks().stream().mapToLong(Track::getDuration).sum();

        if (duration == 0) {
            LOGGER.warn("Channel {} is streaming, but tracks duration is ZERO.", channel.getId());
            return null;
        }

        long trackListPosition = (System.currentTimeMillis() - channel.getStartedAt()) % duration;

        Long trackOffset = 0L;
        for (Track track : channel.getTracks()) {
            if (MathUtil.between(trackOffset, trackOffset + track.getDuration(), trackListPosition)) {
                long position = trackListPosition - trackOffset;
                return new Tuple<>(track, position);
            }
            trackOffset += track.getDuration();
        }

        throw new ChannelControlException(
                String.format("Can't find playing track (channel=%d, position=%d, duration=%d).",
                        channel.getId(), trackListPosition, duration)
        );

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
