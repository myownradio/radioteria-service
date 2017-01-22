package com.radioteria.test.services.channel

import com.radioteria.db.utils.generateUser
import com.radioteria.services.channel.ChannelControlService
import com.radioteria.services.channel.exceptions.ChannelControlServiceException
import com.radioteria.services.channel.impl.ChannelControlServiceImpl
import org.junit.Test
import org.mockito.Mockito.*
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import kotlin.test.*

class ChannelControlServiceTest {

    val eventPublisher: ApplicationEventPublisher = mock(ApplicationEventPublisher::class.java)

    val channelControlService: ChannelControlService = ChannelControlServiceImpl({ 0L }, eventPublisher)

    val user = generateUser()

    @Test
    fun playFromFirst() {
        val channel = user.channels[0]
        val firstTrack = channel.tracks[0]

        channelControlService.playFromFirst(channel)

        assertEquals(firstTrack, channelControlService.nowPlaying(channel).playlistItem.track)

        verifyThatEventIsPublished()
    }

    @Test
    fun playByOrderId() {
        val testOrderId = 2
        val channel = user.channels[0]
        val secondTrack = channel.tracks[1]

        assertTrue { channel.hasPositiveTracksLength }

        channelControlService.playByOrderId(testOrderId, channel)

        assertEquals(secondTrack, channelControlService.nowPlaying(channel).playlistItem.track)

        verifyThatEventIsPublished()
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

        verifyThatEventIsPublished()
    }

    @Test
    fun playNextOnLast() {
        val channel = user.channels[0]
        val firstTrack = channel.tracks.first()
        val lastTrack = channel.tracks.last()

        channelControlService.playByOrderId(lastTrack.orderId, channel)
        channelControlService.playNext(channel)

        assertEquals(firstTrack, channelControlService.nowPlaying(channel).playlistItem.track)

        verifyThatEventIsPublished()
    }

    @Test(expected = ChannelControlServiceException::class)
    fun playNextOnStopped() {
        val channel = user.channels[0]
        channelControlService.playNext(channel)
    }

    @Test
    fun playPrevious() {
        val channel = user.channels[0]
        val secondTrack = channel.tracks[2]
        val expectedTrack = channel.tracks[1]

        channelControlService.playByOrderId(secondTrack.orderId, channel)
        channelControlService.playPrevious(channel)

        assertEquals(expectedTrack, channelControlService.nowPlaying(channel).playlistItem.track)

        verifyThatEventIsPublished()
    }

    @Test
    fun playPreviousOnFirst() {
        val channel = user.channels[0]

        channelControlService.playByOrderId(channel.tracks.first().orderId, channel)
        channelControlService.playPrevious(channel)

        assertEquals(channel.tracks.last(), channelControlService.nowPlaying(channel).playlistItem.track)

        verifyThatEventIsPublished()
    }

    @Test(expected = ChannelControlServiceException::class)
    fun playPreviousOnStopped() {
        val channel = user.channels[0]
        channelControlService.playPrevious(channel)
    }

    @Test
    fun stop() {
        val channel = user.channels[0]
        channelControlService.playFromFirst(channel)
        channelControlService.stop(channel)

        assertFalse { channelControlService.isPlaying(channel) }

        verifyThatEventIsPublished()
    }

    fun verifyThatEventIsPublished() {
        verify(eventPublisher, atLeastOnce()).publishEvent(isA(ApplicationEvent::class.java))
    }

}