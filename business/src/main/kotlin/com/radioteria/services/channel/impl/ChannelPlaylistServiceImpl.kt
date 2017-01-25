package com.radioteria.services.channel.impl

import com.radioteria.db.entities.Channel
import com.radioteria.db.entities.Track
import com.radioteria.services.channel.ChannelControlService
import com.radioteria.services.channel.ChannelPlaylistService
import com.radioteria.services.channel.events.TrackAddedEvent
import com.radioteria.services.channel.events.TrackDeletedEvent
import com.radioteria.services.util.TimeService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class ChannelPlaylistServiceImpl(
        val timeService: TimeService,
        val eventPublisher: ApplicationEventPublisher,
        val channelControlService: ChannelControlService
) : ChannelPlaylistService {

    override fun addTrackToChannel(track: Track, channel: Channel) {
        if (channelControlService.isPlaying(channel)) {
            val playedFullLaps = channel.getFullLapsPlayedAt(timeService.getTimeMillis())!!
            val timeCompensation = playedFullLaps * track.duration
            channelControlService.scroll(-timeCompensation, channel)
        }

        channel.addTrack(track)

        eventPublisher.publishEvent(TrackAddedEvent(this, track, channel))
    }

    override fun removeTrackFromChannel(track: Track, channel: Channel) {
        if (channelControlService.isPlaying(channel)) {
            val playedFullLaps = channel.getFullLapsPlayedAt(timeService.getTimeMillis())!!
            val timeCompensation = playedFullLaps * track.duration
            if (channelControlService.getNowPlaying(channel).`is`(track)) {
                channelControlService.playNext(channel)
            }
            channelControlService.scroll(timeCompensation, channel)
        }

        channel.removeTrack(track)

        eventPublisher.publishEvent(TrackDeletedEvent(this, track, channel))
    }

    override fun moveTrack(track: Track, newOrderId: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun shuffle(channel: Channel) {
        throw UnsupportedOperationException("not implemented")
    }

}