package com.radioteria.services.channel.impl

import com.radioteria.db.entities.Channel
import com.radioteria.db.entities.Track
import com.radioteria.services.channel.ChannelPlaybackService
import com.radioteria.services.channel.ChannelPlaylistService
import com.radioteria.services.channel.events.TrackAddedEvent
import com.radioteria.services.channel.events.TrackDeletedEvent
import com.radioteria.services.util.TimeService
import com.radioteria.util.shuffled
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
            compensatePositionSlipBeforeAdd(track, channel)
        }

        channel.addTrack(track)

        eventPublisher.publishEvent(TrackAddedEvent(this, track, channel))
    }


    override fun removeTrackFromChannel(track: Track, channel: Channel) {
        if (channelPlaybackService.isPlaying(channel)) {
            if (channelPlaybackService.getNowPlaying(channel) equalTo track) {
                channelPlaybackService.restartCurrent(channel)
            }
            compensatePositionSlipBeforeRemove(track, channel)
        }

        channel.removeTrack(track)

        eventPublisher.publishEvent(TrackDeletedEvent(this, track, channel))
    }

    private fun compensatePositionSlipBeforeAdd(track: Track, channel: Channel) {
        val fullLapsPlayed = channel.getFullLapsPlayedAt(timeService.getTimeMillis())!!
        val slipMillis = fullLapsPlayed * track.duration
        channelPlaybackService.scroll(slipMillis, channel)
    }

    private fun compensatePositionSlipBeforeRemove(track: Track, channel: Channel) {
        val trackBeforeCurrent = channelPlaybackService.getNowPlaying(channel).track.orderId > track.orderId
        val fullLapsPlayed = channel.getFullLapsPlayedAt(timeService.getTimeMillis())!!
        val additionalLap = if (trackBeforeCurrent) 1 else 0

        val slipMillis = (fullLapsPlayed + additionalLap) * track.duration

        channelPlaybackService.scroll(-slipMillis, channel)
    }

    override fun moveTrack(track: Track, newOrderId: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun shuffle(channel: Channel) {
        slipSafeAction(channel) {
            val newOrders = (1..channel.tracks.size).shuffled()

            channel.tracks.forEachIndexed { i, track -> track.orderId = newOrders[i] }
            channel.tracks.sortBy { it.orderId }
        }
    }

    private fun slipSafeAction(channel: Channel, block: () -> Unit) {
        if (!channelPlaybackService.isPlaying(channel)) {
            block.invoke()
            return
        }

        val nowPlayingItem = channelPlaybackService
                .getNowPlaying(channel)
                .playlistItem

        block.invoke()

        val foundPlayingItem = channel
                .tracksAsPlaylistItems
                .find { it.track == nowPlayingItem.track }

        if (foundPlayingItem == null) {
            channelPlaybackService.playByOrderId(nowPlayingItem.track.orderId, channel)
            return
        }

        val offsetSlip = foundPlayingItem.offset - nowPlayingItem.offset

        channelPlaybackService.scroll(offsetSlip, channel)
    }

}