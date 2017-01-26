package com.radioteria.db.entities

import com.radioteria.db.entities.meta.FileMeta
import javax.persistence.*

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