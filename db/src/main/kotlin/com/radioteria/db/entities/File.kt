package com.radioteria.db.entities

import javax.persistence.*

object FileMeta {
    const val TABLE_NAME = "files"
    const val NAME = "name"
    const val CONTENT_ID = "content_id"
}

@Entity
@Table(name = FileMeta.TABLE_NAME)
@Access(AccessType.FIELD)
class File(
        @ManyToOne(targetEntity = Content::class)
        @JoinColumn(name = FileMeta.CONTENT_ID, nullable = false)
        var content: Content,

        @Column(name = FileMeta.NAME, nullable = false)
        var name: String = "",

        id: Long? = null
) : IdAwareEntity<Long>(id)