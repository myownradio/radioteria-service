package com.radioteria.db.entities

import com.radioteria.db.entities.meta.ChannelMeta
import com.radioteria.db.entities.meta.TrackMeta
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.*

class NowPlaying(val track: Track, val position: Long)

class TrackWithOffset(val track: Track, val offset: Long)

@Entity
@Table(name = ChannelMeta.TABLE_NAME)
@Access(AccessType.FIELD)
class Channel(
        @ManyToOne(targetEntity = User::class)
        @JoinColumn(name = ChannelMeta.USER_ID, nullable = false)
        var user: User,

        @Column(name = ChannelMeta.NAME, nullable = false)
        var name: String = "",

        @Column(name = ChannelMeta.DESCRIPTION, nullable = false)
        var description: String = "",

        @Column(name = ChannelMeta.STARTED_AT)
        var startedAt: Long? = null,

        @ManyToOne(targetEntity = File::class)
        @JoinColumn(name = ChannelMeta.ARTWORK_FILE_ID)
        var artworkFile: File? = null,

        @OneToMany(mappedBy = "channel", orphanRemoval = true)
        @OrderBy(TrackMeta.ORDER_ID + " ASC")
        var tracks: MutableList<Track> = arrayListOf(),

        id: Long? = null
) : IdAwareEntity<Long>(id) {

    val isControllable: Boolean get() = isStarted && hasTracks
    val isPlaying: Boolean get() = isPlayable && isStarted

    val isStarted: Boolean get() = startedAt != null
    val isPlayable: Boolean get() = hasTracks && hasPositiveTracksLength

    val tracksDuration: Long get() = tracks.map { it.duration }.sum()
    val hasPositiveTracksLength: Boolean get() = tracksDuration > 0
    val hasTracks: Boolean get() = tracks.isNotEmpty()
    val hasNoTracks: Boolean get() = tracks.isEmpty()

    fun addTrack(track: Track) {
        track.channel = this
        track.orderId = tracks.size + 1

        tracks.add(track)
    }

    fun removeTrack(track: Track) {
        tracks.removeAll { it.id == track.id }
    }

    fun moveTimePosition(amount: Long): Unit {
        if (isPlaying) {
            val oldStartPosition = startedAt!!
            val newStartPosition = oldStartPosition - amount
            startedAt == newStartPosition
        }
    }

    fun getTimePositionAt(currentTimeMillis: Long): Long? {
        if (isPlaying) {
            return (currentTimeMillis - startedAt!!) % tracksDuration
        }
        return null
    }

    fun getFullLapsPlayedAt(currentTimeMillis: Long): Long? {
        if (isPlaying) {
            return (currentTimeMillis - startedAt!!) / tracksDuration
        }
        return null
    }

    fun getNowPlaying(currentTimeMillis: Long): NowPlaying? {
        return getTimePositionAt(currentTimeMillis)
                ?.let { getTrackByTimePosition(it) }
    }

    fun getTrackByTimePosition(timePosition: Long): NowPlaying? {
        val offset: AtomicLong = AtomicLong(0L)
        return tracks
                .map { TrackWithOffset(it, offset.getAndAdd(it.duration)) }
                .filter { timePosition in (it.offset until it.offset + it.track.duration) }
                .firstOrNull()
                ?.let { NowPlaying(it.track, timePosition - it.offset) }
    }

}