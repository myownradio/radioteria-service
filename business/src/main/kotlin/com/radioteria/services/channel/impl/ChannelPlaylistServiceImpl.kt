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
import java.util.*

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

    override fun removeTrackFromChannel(track: Track, channel: Channel) {
        if (channelPlaybackService.isPlaying(channel)) {
            if (channelPlaybackService.getNowPlaying(channel) eq track) {
                channelPlaybackService.restartCurrent(channel)
            }
            compensatePositionSlipBeforeRemove(track, channel)
        }

        channel.removeTrack(track)

        eventPublisher.publishEvent(TrackDeletedEvent(this, track, channel))
    }

    private fun compensatePositionSlipBeforeAdd(channel: Channel, track: Track) {
        val fullLapsPlayed = channel.getFullLapsPlayedAt(timeService.getTimeMillis())!!
        val slipMillis = fullLapsPlayed * track.duration
        channelPlaybackService.scroll(slipMillis, channel)
    }

    private fun compensatePositionSlipBeforeRemove(track: Track, channel: Channel) {
        val fullLapsPlayed = channel.getFullLapsPlayedAt(timeService.getTimeMillis())!!
        val slipMillis = fullLapsPlayed * track.duration

        channelPlaybackService.scroll(-slipMillis, channel)
    }

    override fun moveTrack(track: Track, newOrderId: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun shuffle(channel: Channel) {
        slipSafeAction(channel) {
            val random = Random()

            val newOrders = (1..channel.tracks.size)
                    .sortedBy { random.nextInt(channel.tracks.size) }

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

        val newPlayingItem = channel.tracksAsPlaylistItems.find { it.track == nowPlayingItem.track }

        if (newPlayingItem != null) {
            val slip = newPlayingItem.offset - nowPlayingItem.offset
            channelPlaybackService.scroll(slip, channel)
        }

        channelPlaybackService.playByOrderId(nowPlayingItem.track.orderId, channel)
    }

}