package com.radioteria.test.services.channel

import com.radioteria.db.utils.generateUser
import com.radioteria.services.channel.ChannelControlService
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
class ChannelControlServiceTest {

    @Autowired
    lateinit var channelControlService: ChannelControlService

    val user = generateUser(channelsAmount = 1, tracksPerChannel = 10)

    @Test
    fun playFromFirst() {
        val channel = user.channels.first()
        val firstTrack = channel.tracks.first()

        channelControlService.playFromFirst(channel)

        assertEquals(firstTrack, channelControlService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test
    fun playByOrderId() {
        val testOrderId = 2
        val channel = user.channels.first()
        val secondTrack = channel.tracks[1]

        assertTrue { channel.hasPositiveTracksLength }

        channelControlService.playByOrderId(testOrderId, channel)

        assertEquals(secondTrack, channelControlService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test
    fun playByTrackId() {
        val channel = user.channels.first()
        val secondTrack = channel.tracks[1]
        val testTrackId = secondTrack.id!!

        assertTrue { channel.hasPositiveTracksLength }

        channelControlService.playByTrackId(testTrackId, channel)

        assertEquals(secondTrack, channelControlService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }


    @Test(expected = ChannelControlServiceException::class)
    fun playByWrongOrderId() {
        val wrongOrderId = 20
        val channel = user.channels.first()

        channelControlService.playByOrderId(wrongOrderId, channel)
    }

    @Test
    fun playNext() {
        val channel = user.channels.first()
        val expectedTrack = channel.tracks[1]

        channelControlService.playFromFirst(channel)
        channelControlService.playNext(channel)

        assertEquals(expectedTrack, channelControlService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test
    fun playNextOnLast() {
        val channel = user.channels.first()
        val firstTrack = channel.tracks.first()
        val lastTrack = channel.tracks.last()

        channelControlService.playByOrderId(lastTrack.orderId, channel)
        channelControlService.playNext(channel)

        assertEquals(firstTrack, channelControlService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test(expected = ChannelControlServiceException::class)
    fun playNextOnStopped() {
        val channel = user.channels[0]
        channelControlService.playNext(channel)
    }

    @Test
    fun playPrevious() {
        val channel = user.channels.first()
        val secondTrack = channel.tracks[2]
        val expectedTrack = channel.tracks[1]

        channelControlService.playByOrderId(secondTrack.orderId, channel)
        channelControlService.playPrevious(channel)

        assertEquals(expectedTrack, channelControlService.getNowPlaying(channel).track)

        verifyThatEventIsPublished()
    }

    @Test
    fun playPreviousOnFirst() {
        val channel = user.channels[0]

        channelControlService.playByOrderId(channel.tracks.first().orderId, channel)
        channelControlService.playPrevious(channel)

        assertEquals(channel.tracks.last(), channelControlService.getNowPlaying(channel).track)

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
//        assertTrue { eventPublisher.hasPublished(ChannelControlEvent::class.java) }
    }

}