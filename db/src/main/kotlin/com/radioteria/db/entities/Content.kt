package com.radioteria.db.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

object ContentMeta {
    const val TABLE_NAME = "content"
    const val HASH = "hash"
    const val CONTENT_TYPE = "content_type"
    const val LENGTH = "length"
}

@Entity
@Table(name = ContentMeta.TABLE_NAME)
class Content(
        @Column(name = ContentMeta.HASH, nullable = false, unique = true)
        val hash: String = "",

        @Column(name = ContentMeta.CONTENT_TYPE, nullable = false)
        val contentType: String = "",

        @Column(name = ContentMeta.LENGTH, nullable = false)
        val length: Long = 0,

        id: Long? = null
) : IdAwareEntity<Long>(id)