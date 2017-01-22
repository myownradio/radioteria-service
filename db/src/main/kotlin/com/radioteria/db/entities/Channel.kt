package com.radioteria.db.entities

import com.radioteria.db.entities.data.*
import com.radioteria.db.entities.meta.ChannelMeta
import com.radioteria.db.entities.meta.TrackMeta
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.*

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

    /**
     * Is channel could be controlled.
     */
    val isControllable: Boolean get() = isStarted && hasTracks

    /**
     * Is channel playing.
     */
    val isPlaying: Boolean get() = isPlayable && isStarted

    /**
     * Is channel is started.
     */
    val isStarted: Boolean get() = startedAt != null

    /**
     * Is channel could play.
     */
    val isPlayable: Boolean get() = hasTracks && hasPositiveTracksLength

    /**
     * All tracks duration.
     */
    val tracksDuration: Long get() = tracks.map { it.duration }.sum()

    /**
     * Is channel has tracks with positive summary length.
     */
    val hasPositiveTracksLength: Boolean get() = tracksDuration > 0

    /**
     * Is channel has tracks attached.
     */
    val hasTracks: Boolean get() = tracks.isNotEmpty()

    /**
     * If channel has no tracks attached.
     */
    val hasNoTracks: Boolean get() = tracks.isEmpty()

    /**
     * View tracks as playlist items with offset.
     */
    val tracksAsPlaylistItems: List<PlaylistItem> get() {
        val offset: AtomicLong = AtomicLong(0L)

        return tracks.map { PlaylistItem(it, offset.getAndAdd(it.duration)) }
    }

    fun addTrack(track: Track) {
        if (track.channel.id != id) {
            throw IllegalArgumentException("Track is not attached to this channel.")
        }

        track.orderId = tracks.size + 1

        tracks.add(track)
    }

    fun removeTrack(track: Track) {
        tracks.removeAll { it.id == track.id }
    }

    fun moveTimePosition(amount: Long): Unit {
        if (isControllable) {
            val oldStartPosition = startedAt!!
            val newStartPosition = oldStartPosition - amount
            startedAt == newStartPosition
        }
    }

    fun getTimePositionAt(currentTimeMillis: Long): Long? {
        return getFullTimePosition(currentTimeMillis)
                ?. let { it % tracksDuration }
    }

    fun getFullLapsPlayedAt(currentTimeMillis: Long): Long? {
        return getFullTimePosition(currentTimeMillis)
                ?. let { it / tracksDuration }
    }

    internal fun getFullTimePosition(currentTimeMillis: Long): Long? {
        return if (isPlaying) { currentTimeMillis - startedAt!! } else { null }
    }

    fun getNowPlaying(currentTimeMillis: Long): NowPlaying? {
        return getTimePositionAt(currentTimeMillis)
                ?.let { getTrackByTimePosition(it) }
    }

    fun getTrackByTimePosition(timePosition: Long): NowPlaying? {
        return tracksAsPlaylistItems
                .firstOrNull { it.isPlayingAt(timePosition) }
                ?.makeNowPlayingAt(timePosition)
    }

}