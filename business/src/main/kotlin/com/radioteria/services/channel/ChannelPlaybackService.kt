package com.radioteria.services.channel

import com.radioteria.db.entities.Channel
import com.radioteria.db.entities.data.NowPlaying

interface ChannelPlaybackService {
    fun playFromFirst(channel: Channel): Unit
    fun playByOrderId(orderId: Int, channel: Channel): Unit
    fun playByTrackId(trackId: Long, channel: Channel): Unit
    fun stop(channel: Channel): Unit
    fun playNext(channel: Channel): Unit
    fun playPrevious(channel: Channel): Unit
    fun scroll(amountMillis: Long, channel: Channel): Unit
    fun restartCurrent(channel: Channel): Unit

    fun isPlaying(channel: Channel): Boolean
    fun isControllable(channel: Channel): Boolean
    fun isPlayable(channel: Channel): Boolean

    fun getNowPlaying(channel: Channel): NowPlaying
    fun getTimePosition(channel: Channel): Long?
}