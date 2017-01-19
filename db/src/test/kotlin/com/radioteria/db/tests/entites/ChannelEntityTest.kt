package com.radioteria.db.tests.entites

import com.radioteria.db.entities.Channel
import com.radioteria.db.entities.User
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChannelEntityTest {
    @Test
    fun emptyStoppedChannel() {
        val channel = Channel(User())

        assertTrue { channel.hasNoTracks }

        assertFalse { channel.isStarted }
        assertFalse { channel.isPlaying }
        assertFalse { channel.isPlayable }
        assertFalse { channel.isControllable }
        assertFalse { channel.hasTracks }
    }
}