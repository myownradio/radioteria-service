package com.radioteria.test.services.channel

import com.radioteria.db.utils.generateUser
import com.radioteria.services.channel.exceptions.ChannelControlServiceException
import com.radioteria.services.channel.impl.ChannelControlServiceImpl
import org.junit.Test
import org.mockito.Mockito.mock
import org.springframework.context.ApplicationEventPublisher
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChannelControlServiceTest {

    val eventPublisher: ApplicationEventPublisher = mock(ApplicationEventPublisher::class.java)

    val channelControlService = ChannelControlServiceImpl({ 0L }, eventPublisher)

    val user = generateUser()

    @Test
    fun playFromFirst() {
        val channel = user.channels[0]
        val firstTrack = channel.tracks[0]

        channelControlService.playFromFirst(channel)

        assertEquals(firstTrack, channelControlService.nowPlaying(channel).playlistItem.track)
    }

    @Test
    fun playByOrderId() {
        val testOrderId = 2
        val channel = user.channels[0]
        val secondTrack = channel.tracks[1]

        assertTrue { channel.hasPositiveTracksLength }

        channelControlService.playByOrderId(testOrderId, channel)

        assertEquals(secondTrack, channelControlService.nowPlaying(channel).playlistItem.track)
    }

    @Test(expected = ChannelControlServiceException::class)
    fun playByWrongOrderId() {
        val wrongOrderId = 20
        val channel = user.channels[0]

        channelControlService.playByOrderId(wrongOrderId, channel)
    }

    @Test
    fun playNext() {
        val channel = user.channels[0]
        val expectedTrack = channel.tracks[1]

        channelControlService.playFromFirst(channel)
        channelControlService.playNext(channel)

        assertEquals(expectedTrack, channelControlService.nowPlaying(channel).playlistItem.track)
    }

    @Test
    fun playNextOnLast() {
        val channel = user.channels[0]
        val firstTrack = channel.tracks.first()
        val lastTrack = channel.tracks.last()

        channelControlService.playByOrderId(lastTrack.orderId, channel)
        channelControlService.playNext(channel)

        assertEquals(firstTrack, channelControlService.nowPlaying(channel).playlistItem.track)
    }

}