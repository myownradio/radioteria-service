package com.radioteria.db.entities.data

import com.radioteria.db.entities.Track

data class PlaylistItem(val track: Track, val offset: Long) {
    val leftBound: Long get() = offset
    val rightBound: Long get() = offset + track.duration

    fun makeNowPlayingAt(timePosition: Long): NowPlaying =
            NowPlaying(this, timePosition - this.offset)

    fun isPlayingAt(timePosition: Long): Boolean =
            timePosition in (leftBound until rightBound)
}