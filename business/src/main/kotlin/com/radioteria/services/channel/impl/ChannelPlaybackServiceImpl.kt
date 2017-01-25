package com.radioteria.services.channel.impl

import com.radioteria.services.exceptions.ServiceException

import com.radioteria.db.entities.Channel
import com.radioteria.db.entities.data.NowPlaying
import com.radioteria.services.channel.ChannelPlaybackService
import com.radioteria.services.channel.events.ChannelControlEvent
import com.radioteria.services.channel.exceptions.ChannelControlServiceException
import com.radioteria.services.util.TimeService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
@Service
class ChannelPlaybackServiceImpl(
        val timeService: TimeService,
        val eventPublisher: ApplicationEventPublisher
) : ChannelPlaybackService {

    override fun playFromStart(channel: Channel) {
        playFromTimePosition(0L, channel)
    }

    override fun playFromTimePosition(timePosition: Long, channel: Channel) {
        if (!isPlayable(channel)) {
            throw ServiceException("Channel could not be played.")
        }
        channel.startedAt = timeService.getTimeMillis() - timePosition

        publishEvent(channel)
    }

    override fun playByOrderId(orderId: Int, channel: Channel) {
        val playlistItem = channel.tracksAsPlaylistItems
                .find { it.track.orderId == orderId }
                ?: channel.tracksAsPlaylistItems
                .firstOrNull()
                ?: throw ChannelControlServiceException("No track with order position $orderId exists on the channel.")

        playFromTimePosition(playlistItem.offset, channel)
    }

    override fun playByTrackId(trackId: Long, channel: Channel) {
        val playlistItem = channel.tracksAsPlaylistItems
                .find { it.track.id == trackId }
                ?: throw ChannelControlServiceException("No track with id $trackId exists on the channel.")

        playFromTimePosition(playlistItem.offset, channel)
    }

    override fun restartCurrent(channel: Channel) {
        val nowPlaying = getNowPlaying(channel)

        playByOrderId(nowPlaying.playlistItem.track.orderId, channel)
    }

    override fun stop(channel: Channel) {
        channel.startedAt = null

        publishEvent(channel)
    }

    override fun playNext(channel: Channel) {
        val nowPlaying = getNowPlaying(channel)
        val lastOrderId = channel.tracks.last().orderId

        if (nowPlaying.playlistItem.track.orderId == lastOrderId) {
            return playByOrderId(1, channel)
        }

        playByOrderId(nowPlaying.playlistItem.track.orderId + 1, channel)
    }

    override fun playPrevious(channel: Channel) {
        val nowPlaying = getNowPlaying(channel)
        val lastOrderId = channel.tracks.last().orderId

        if (nowPlaying.playlistItem.track.orderId == 1) {
            return playByOrderId(lastOrderId, channel)
        }

        playByOrderId(nowPlaying.playlistItem.track.orderId - 1, channel)
    }

    override fun scroll(amountMillis: Long, channel: Channel) {
        if (!isPlaying(channel)) {
            throw ChannelControlServiceException("Channel is stopped.")
        }

        channel.scroll(amountMillis)

        publishEvent(channel)
    }

    override fun isPlaying(channel: Channel): Boolean {
        return channel.isPlaying
    }

    override fun isControllable(channel: Channel): Boolean {
        return channel.isControllable
    }

    override fun isPlayable(channel: Channel): Boolean {
        return channel.isPlayable
    }

    override fun getNowPlaying(channel: Channel): NowPlaying {
        return channel.getNowPlaying(timeService.getTimeMillis())
                ?: throw ChannelControlServiceException("Channel is stopped.")
    }

    override fun getTimePosition(channel: Channel): Long? {
        return channel.getTimePositionAt(timeService.getTimeMillis());
    }

    internal fun publishEvent(channel: Channel) {
        eventPublisher.publishEvent(ChannelControlEvent(this, channel))
    }

}