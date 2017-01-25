package com.radioteria.test.services.channel

import com.radioteria.db.utils.generateUser
import com.radioteria.services.channel.ChannelPlaybackService
import com.radioteria.services.channel.exceptions.ChannelControlServiceException
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import kotlin.test.*

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = arrayOf("classpath:business-context.xml"))
@ActiveProfiles("test")
class ChannelPlaybackServiceTest {

    @Autowired
    lateinit var channelPlaybackService: ChannelPlaybackService

    val user = generateUser(channelsAmount = 1, tracksPerChannel = 10)

    @Test
    fun playFromFirst() {
        val channel = user.channels.first()
        val firstTrack = channel.tracks.first()

        channelPlaybackService.playFromStart(channel)

        assertEquals(firstTrack, channelPlaybackService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test
    fun playByOrderId() {
        val testOrderId = 2
        val channel = user.channels.first()
        val secondTrack = channel.tracks[1]

        assertTrue { channel.hasPositiveTracksLength }

        channelPlaybackService.playByOrderId(testOrderId, channel)

        assertEquals(secondTrack, channelPlaybackService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test
    fun playByTrackId() {
        val channel = user.channels.first()
        val secondTrack = channel.tracks[1]
        val testTrackId = secondTrack.id!!

        assertTrue { channel.hasPositiveTracksLength }

        channelPlaybackService.playByTrackId(testTrackId, channel)

        assertEquals(secondTrack, channelPlaybackService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }


    @Test(expected = ChannelControlServiceException::class)
    fun playByWrongOrderId() {
        val wrongOrderId = 20
        val channel = user.channels.first()

        channelPlaybackService.playByOrderId(wrongOrderId, channel)
    }

    @Test
    fun playNext() {
        val channel = user.channels.first()
        val expectedTrack = channel.tracks[1]

        channelPlaybackService.playFromStart(channel)
        channelPlaybackService.playNext(channel)

        assertEquals(expectedTrack, channelPlaybackService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test
    fun playNextOnLast() {
        val channel = user.channels.first()
        val firstTrack = channel.tracks.first()
        val lastTrack = channel.tracks.last()

        channelPlaybackService.playByOrderId(lastTrack.orderId, channel)
        channelPlaybackService.playNext(channel)

        assertEquals(firstTrack, channelPlaybackService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test(expected = ChannelControlServiceException::class)
    fun playNextOnStopped() {
        val channel = user.channels[0]
        channelPlaybackService.playNext(channel)
    }

    @Test
    fun playPrevious() {
        val channel = user.channels.first()
        val secondTrack = channel.tracks[2]
        val expectedTrack = channel.tracks[1]

        channelPlaybackService.playByOrderId(secondTrack.orderId, channel)
        channelPlaybackService.playPrevious(channel)

        assertEquals(expectedTrack, channelPlaybackService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test
    fun playPreviousOnFirst() {
        val channel = user.channels[0]

        channelPlaybackService.playByOrderId(channel.tracks.first().orderId, channel)
        channelPlaybackService.playPrevious(channel)

        assertEquals(channel.tracks.last(), channelPlaybackService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test(expected = ChannelControlServiceException::class)
    fun playPreviousOnStopped() {
        val channel = user.channels[0]
        channelPlaybackService.playPrevious(channel)
    }

    @Test
    fun stop() {
        val channel = user.channels[0]
        channelPlaybackService.playFromStart(channel)
        channelPlaybackService.stop(channel)

        assertFalse { channelPlaybackService.isPlaying(channel) }

        verifyThatEventIsPublished()
    }

    fun verifyThatEventIsPublished() {
//        assertTrue { eventPublisher.hasPublished(ChannelControlEvent::class.java) }
    }

}