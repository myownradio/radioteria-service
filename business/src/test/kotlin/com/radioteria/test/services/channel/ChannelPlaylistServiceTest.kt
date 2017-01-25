package com.radioteria.test.services.channel

import com.radioteria.db.entities.*
import com.radioteria.db.entities.data.NowPlaying
import com.radioteria.db.entities.data.PlaylistItem
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

    val channel: Channel = generateChannel(tracks = 10)

    @Test
    fun addTrackAfterCurrentTimePosition() {
        startChannelFromTheBeginning()
        addNewTrackToChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun addTrackBeforeCurrentTimePosition() {
        startChannelFromSecondLap()
        addNewTrackToChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun removeTrackAfterCurrentTimePosition() {
        startChannelFromTheBeginning()
        removeLastTrackFromChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun removeTrackWithCompensation() {
        startChannelFromSecondLap()
        removeLastTrackFromChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun removeCurrentTrackWithoutCompensation() {
        startChannelFromTheBeginning()
        removeCurrentTrackFromChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun removeCurrentTrackWithCompensation() {
        startChannelFromSecondLap()
        removeCurrentTrackFromChannel()
        verifyThatWeAreAtTheBeginning()
    }

    @Test
    fun removeTrackBeforeCurrentAtFirstLap() {
        startChannelFromTheBeginning()
        rewindToNextTrack()
        verifyThatNoSlipOccurred {
            removeFirstTrackFromChannel()
        }
    }

    @Test
    fun shuffleOnFirstLap() {
        startChannelFromTheBeginning()
        rewindToNextTrack()
        verifyThatNoSlipOccurred {
            shuffleTracksInChannel()
        }
    }

    @Test
    fun shuffleOnNextLap() {
        startChannelFromSecondLap()
        rewindToNextTrack()
        verifyThatNoSlipOccurred {
            shuffleTracksInChannel()
        }
    }

    private fun startChannelFromTheBeginning() {
        channelPlaybackService.playFromStart(channel)
    }

    private fun startChannelFromSecondLap() {
        channelPlaybackService.playFromTimePosition(channel.tracksDuration, channel)
    }

    private fun verifyThatNoSlipOccurred(block: () -> Unit) {
        val playingBefore = getNowPlaying()
        block.invoke()
        val playingAfter = getNowPlaying()

        assertEquals(playingBefore.track, playingAfter.track)
        assertEquals(playingBefore.trackPosition, playingAfter.trackPosition)
    }

    private fun getNowPlaying(): NowPlaying {
        return channelPlaybackService.getNowPlaying(channel)
    }

    private fun shuffleTracksInChannel() {
        channelPlaylistService.shuffle(channel)
    }

    private fun addNewTrackToChannel() {
        val trackToAdd = Track(duration = 500, channel = channel, trackFile = File(Content()))

        channelPlaylistService.addTrackToChannel(trackToAdd, channel)
    }

    private fun removeFirstTrackFromChannel() {
        val firstTrack = channel.tracks.first()

        channelPlaylistService.removeTrackFromChannel(firstTrack, channel)
    }

    private fun removeCurrentTrackFromChannel() {
        val currentTrack = channelPlaybackService
                .getNowPlaying(channel)
                .track

        channelPlaylistService.removeTrackFromChannel(currentTrack, channel)
    }

    private fun removeLastTrackFromChannel() {
        val lastTrack = channel.tracks.last()

        channelPlaylistService.removeTrackFromChannel(lastTrack, channel)
    }

    private fun rewindTimePositionToNextLap() {
        val tracksDuration = channel.tracksDuration
        channelPlaybackService.scroll(tracksDuration, channel)
    }

    private fun rewindToNextTrack() {
        channelPlaybackService.playNext(channel)
    }

    private fun rewindToPreviousTrack() {
        channelPlaybackService.playPrevious(channel)
    }

    private fun verifyThatWeAreAtTheBeginning() {
        assertEquals(0, channelPlaybackService.getTimePosition(channel))
    }
}