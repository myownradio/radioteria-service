package com.radioteria.services.channel.impl

import com.radioteria.services.exceptions.ServiceException
import com.radioteria.db.entities.Channel
import com.radioteria.db.entities.data.NowPlaying
import com.radioteria.services.channel.ChannelControlService
import com.radioteria.services.channel.events.ChannelControlsEvent
import com.radioteria.services.channel.exceptions.ChannelControlServiceException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class ChannelControlServiceImpl(
        val realTimeProvider: () -> Long,
        val eventPublisher: ApplicationEventPublisher
) : ChannelControlService {

    override fun playFromFirst(channel: Channel) {
        playFromTimePosition(0L, channel)
    }

    override fun playByOrderId(orderId: Int, channel: Channel) {
        val playlistItem = channel.tracksAsPlaylistItems
                .find { it.track.orderId == orderId }
                ?: throw ChannelControlServiceException("No track at position $orderId exists.")
        playFromTimePosition(playlistItem.offset, channel)
    }

    internal fun playFromTimePosition(timePosition: Long, channel: Channel) {
        if (!isPlayable(channel)) {
            throw ServiceException("Channel could not be played.")
        }
        channel.startedAt = realTimeProvider.invoke() - timePosition

        publishEvent(channel)
    }

    override fun stop(channel: Channel) {
        channel.startedAt = null

        publishEvent(channel)
    }

    override fun playNext(channel: Channel) {
        val nowPlaying = nowPlaying(channel)
        val lastOrderId = channel.tracks.last().orderId

        if (nowPlaying.playlistItem.track.orderId == lastOrderId) {
            return playByOrderId(1, channel)
        }

        playByOrderId(nowPlaying.playlistItem.track.orderId + 1, channel)
    }

    override fun playPrevious(channel: Channel) {
        val nowPlaying = nowPlaying(channel)
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

    override fun nowPlaying(channel: Channel): NowPlaying {
        return channel.getNowPlaying(realTimeProvider.invoke())
                ?: throw ChannelControlServiceException("Channel is stopped.")
    }

    internal fun publishEvent(channel: Channel) {
        eventPublisher.publishEvent(ChannelControlsEvent(this, channel))
    }

}