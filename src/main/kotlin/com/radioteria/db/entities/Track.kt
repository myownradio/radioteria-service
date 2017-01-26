package com.radioteria.db.entities

import com.radioteria.db.entities.meta.TrackMeta
import javax.persistence.*

@Entity
@Table(name = TrackMeta.TABLE_NAME)
@Access(AccessType.FIELD)
class Track(
        @ManyToOne(targetEntity = File::class)
        @JoinColumn(name = TrackMeta.TRACK_FILE_ID, nullable = false)
        var trackFile: File,

        @ManyToOne(targetEntity = Channel::class)
        @JoinColumn(name = TrackMeta.CHANNEL_ID, nullable = false)
        var channel: Channel,

        @Column(name = TrackMeta.TITLE, nullable = false)
        var title: String = "",

        @Column(name = TrackMeta.ARTIST, nullable = false)
        var artist: String = "",

        @Column(name = TrackMeta.DURATION, nullable = false)
        var duration: Long = 0,

        @Column(name = TrackMeta.ORDER_ID, nullable = false)
        var orderId: Int = 0,

        id: Long? = null
) : IdAwareEntity<Long>(id) {
    override fun toString(): String {
        return "Track(title=${title}, orderId=${orderId})";
    }
}