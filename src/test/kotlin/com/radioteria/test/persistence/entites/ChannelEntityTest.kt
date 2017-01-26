package com.radioteria.test.persistence.entites

import com.radioteria.db.entities.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ChannelEntityTest {
    @Test fun emptyStoppedChannel() {
        val channel = Channel(User())
        val anyTime = System.currentTimeMillis()

        assertTrue { channel.hasNoTracks }

        assertFalse { channel.isStarted }
        assertFalse { channel.isPlaying }
        assertFalse { channel.isPlayable }
        assertFalse { channel.isControllable }
        assertFalse { channel.hasTracks }

        assertNull(channel.getTimePositionAt(anyTime))
        assertNull(channel.getFullLapsPlayedAt(anyTime))
    }

    @Test fun stoppedChannelWithTracks() {
        val channel = Channel(User())
        val trackFile = File(content = Content())
        val anyTime = System.currentTimeMillis()

        channel.addTrack(Track(channel = channel, trackFile = trackFile, duration = 100))
        channel.addTrack(Track(channel = channel, trackFile = trackFile, duration = 200))
        channel.addTrack(Track(channel = channel, trackFile = trackFile, duration = 300))

        assertTrue { channel.hasTracks }
        assertTrue { channel.isPlayable }

        assertFalse { channel.hasNoTracks }
        assertFalse { channel.isStarted }
        assertFalse { channel.isPlaying }
        assertFalse { channel.isControllable }

        assertNull(channel.getTimePositionAt(anyTime))
        assertNull(channel.getFullLapsPlayedAt(anyTime))
    }

    @Test fun playingChannelWithTracks() {
        val startTime = 1000L
        val playTime = 1150L

        val channel = Channel(User(), startedAt = startTime)
        val trackFile = File(content = Content())

        channel.addTrack(Track(channel = channel, title = "Track 1", trackFile = trackFile, duration = 100))
        channel.addTrack(Track(channel = channel, title = "Track 2", trackFile = trackFile, duration = 200))
        channel.addTrack(Track(channel = channel, title = "Track 3", trackFile = trackFile, duration = 300))

        assertTrue { channel.hasTracks }
        assertTrue { channel.isPlayable }
        assertTrue { channel.isStarted }
        assertTrue { channel.isPlaying }
        assertTrue { channel.isControllable }

        assertFalse { channel.hasNoTracks }

        assertEquals(150L, channel.getTimePositionAt(playTime))
        assertEquals(0, channel.getFullLapsPlayedAt(playTime))

        assertEquals("Track 2", channel.getNowPlaying(playTime)?.playlistItem?.track?.title)
    }

    @Test fun channelContainsZeroTracks() {
        val channel = Channel(User())
        val trackFile = File(content = Content())

        channel.addTrack(Track(channel = channel, trackFile = trackFile, duration = 0))
        channel.addTrack(Track(channel = channel, trackFile = trackFile, duration = 0))
        channel.addTrack(Track(channel = channel, trackFile = trackFile, duration = 0))

        assertTrue { channel.hasTracks }

        assertFalse { channel.isControllable }
        assertFalse { channel.isPlayable }
        assertFalse { channel.hasPositiveTracksLength }
    }
}