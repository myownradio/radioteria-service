package com.radioteria.db.entities

import javax.persistence.*

object TrackMeta {
    const val TABLE_NAME = "tracks"
    const val TITLE = "title"
    const val ARTIST = "artist"
    const val DURATION = "duration"
    const val TRACK_FILE_ID = "track_file_id"
    const val CHANNEL_ID = "channel_id"
    const val ORDER_ID = "order_id"
}

@Entity
@Table(name = TrackMeta.TABLE_NAME)
class Track(
        @ManyToOne(targetEntity = File::class)
        @JoinColumn(name = TrackMeta.TRACK_FILE_ID, nullable = false)
        val trackFile: File,

        @ManyToOne(targetEntity = Channel::class)
        @JoinColumn(name = TrackMeta.CHANNEL_ID, nullable = false)
        val channel: Channel,

        @Column(name = TrackMeta.TITLE, nullable = false)
        val title: String = "",

        @Column(name = TrackMeta.ARTIST, nullable = false)
        val artist: String = "",

        @Column(name = TrackMeta.DURATION, nullable = false)
        val duration: Long = 0,

        @Column(name = TrackMeta.ORDER_ID, nullable = false)
        val orderId: Int = 0,

        id: Long? = null
) : IdAwareEntity<Long>(id)