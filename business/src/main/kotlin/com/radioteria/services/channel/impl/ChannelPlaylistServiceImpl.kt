package com.radioteria.services.channel.impl

import com.radioteria.db.entities.Channel
import com.radioteria.db.entities.Track
import com.radioteria.services.channel.ChannelPlaybackService
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
        val channelPlaybackService: ChannelPlaybackService
) : ChannelPlaylistService {

    override fun addTrackToChannel(track: Track, channel: Channel) {
        if (channelPlaybackService.isPlaying(channel)) {
            compensatePositionSlipBeforeAdd(channel, track)
        }

        channel.addTrack(track)

        eventPublisher.publishEvent(TrackAddedEvent(this, track, channel))
    }

    private fun compensatePositionSlipBeforeAdd(channel: Channel, track: Track) {
        val fullLapsPlayed = channel.getFullLapsPlayedAt(timeService.getTimeMillis())!!
        val slipMillis = fullLapsPlayed * track.duration
        channelPlaybackService.scroll(slipMillis, channel)
    }

    override fun removeTrackFromChannel(track: Track, channel: Channel) {
        if (channelPlaybackService.isPlaying(channel)) {
            val playedFullLaps = channel.getFullLapsPlayedAt(timeService.getTimeMillis())!!
            val timeCompensation = playedFullLaps * track.duration
            if (channelPlaybackService.getNowPlaying(channel).`is`(track)) {
                channelPlaybackService.playNext(channel)
            }
            channelPlaybackService.scroll(timeCompensation, channel)
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