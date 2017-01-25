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

    val channel: Channel = generateChannel(user = User(), tracksPerChannel = 10)

    @Test
    fun addTrackWithoutTimeCompensation() {
        startChannelFromTheBeginning()
        addNewTrackToChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun addTrackWithCompensation() {
        startChannelFromTheBeginning()
        moveTimePositionToNextLap()
        addNewTrackToChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun removeTrackWithoutCompensation() {
        startChannelFromTheBeginning()
        removeLastTrackFromChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun removeTrackWithCompensation() {
        startChannelFromTheBeginning()
        moveTimePositionToNextLap()
        removeLastTrackFromChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun removePlayingTrackWithoutCompensation() {
        startChannelFromTheBeginning()
        removeFirstTrackFromChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun removePlayingTrackWithCompensation() {
        startChannelFromTheBeginning()
        moveTimePositionToNextLap()
        removeFirstTrackFromChannel()
        verifyThatWeAreAtTheBeginning()
    }

    private fun addNewTrackToChannel() {
        val trackToAdd = Track(duration = 500, channel = channel, trackFile = File(Content()))

        channelPlaylistService.addTrackToChannel(trackToAdd, channel)
    }

    private fun removeFirstTrackFromChannel() {
        val firstTrack = channel.tracks.first()

        channelPlaylistService.removeTrackFromChannel(firstTrack, channel)
    }

    private fun removeLastTrackFromChannel() {
        val lastTrack = channel.tracks.last()

        channelPlaylistService.removeTrackFromChannel(lastTrack, channel)
    }

    private fun moveTimePositionToNextLap() {
        val tracksDuration = channel.tracksDuration
        channelPlaybackService.scroll(tracksDuration, channel)
    }

    private fun startChannelFromTheBeginning() {
        channelPlaybackService.playFromFirst(channel)
    }

    private fun verifyThatWeAreAtTheBeginning() {
        assertEquals(0, channelPlaybackService.getTimePosition(channel))
    }
}