package com.radioteria.test.services.channel

import com.radioteria.db.entities.*
import com.radioteria.db.utils.generateChannel
import com.radioteria.services.channel.ChannelPlaybackService
import com.radioteria.services.channel.ChannelPlaylistService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import javax.annotation.Resource
import kotlin.test.assertEquals

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = arrayOf("classpath:business-context.xml"))
@ActiveProfiles("test")
class ChannelPlaylistServiceTest {

    @Resource
    lateinit var channelPlaybackService: ChannelPlaybackService

    @Resource
    lateinit var channelPlaylistService: ChannelPlaylistService

    val channelToTest: Channel = generateChannel(user = User(), tracksPerChannel = 10)

    @Test
    fun addTrackWithoutTimeCompensation() {
        startChannelFromBeginning()

        val oldNowPlaying = channelPlaybackService.getNowPlaying(channelToTest)

        addNewTrackToChannel()

        val newNowPlaying = channelPlaybackService.getNowPlaying(channelToTest)

        assertEquals(oldNowPlaying, newNowPlaying)
    }

    @Test
    fun addTrackWithCompensation() {
        startChannelFromBeginning()
        moveTimePositionToNextLap()

        val oldNowPlaying = channelPlaybackService.getNowPlaying(channelToTest)

        addNewTrackToChannel()

        val newNowPlaying = channelPlaybackService.getNowPlaying(channelToTest)

        assertEquals(oldNowPlaying, newNowPlaying)
    }

    private fun addNewTrackToChannel() {
        val trackToAdd = Track(duration = 500, channel = channelToTest, trackFile = File(Content()))

        channelPlaylistService.addTrackToChannel(trackToAdd, channelToTest)
    }

    private fun moveTimePositionToNextLap() {
        val tracksDuration = channelToTest.tracksDuration
        channelPlaybackService.scroll(tracksDuration, channelToTest)
    }

    private fun startChannelFromBeginning() {
        channelPlaybackService.playFromFirst(channelToTest)
    }
}