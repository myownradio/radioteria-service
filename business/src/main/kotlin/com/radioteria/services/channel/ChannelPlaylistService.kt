package com.radioteria.services.channel

import com.radioteria.db.entities.Channel
import com.radioteria.db.entities.Track

interface ChannelPlaylistService {
    fun addTrackToChannel(track: Track, channel: Channel)
    fun removeTrackFromChannel(track: Track, channel: Channel)
    fun moveTrack(track: Track, newOrderId: Int)
    fun shuffle(channel: Channel)
}