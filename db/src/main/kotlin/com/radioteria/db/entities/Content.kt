package com.radioteria.db.entities

import javax.persistence.*

object ContentMeta {
    const val TABLE_NAME = "content"
    const val HASH = "hash"
    const val CONTENT_TYPE = "content_type"
    const val LENGTH = "length"
}

@Entity
@Table(name = ContentMeta.TABLE_NAME)
@Access(AccessType.FIELD)
class Content(
        @Column(name = ContentMeta.HASH, nullable = false, unique = true)
        var hash: String = "",

        @Column(name = ContentMeta.CONTENT_TYPE, nullable = false)
        var contentType: String = "application/octet-stream",

        @Column(name = ContentMeta.LENGTH, nullable = false)
        var length: Long = 0,

        id: Long? = null
) : IdAwareEntity<Long>(id)