package com.radioteria.db.entities.data

import com.radioteria.db.entities.Track

data class NowPlaying(val playlistItem: PlaylistItem, val trackPosition: Long) {
    fun `is`(track: Track): Boolean = track.id == playlistItem.track.id
}
