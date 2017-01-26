package com.radioteria.db.entities

import java.io.Serializable
import javax.persistence.*


@MappedSuperclass
abstract class IdAwareEntity<K : Serializable>(
        @Id
        @GeneratedValue
        @Column(name = "id")
        var id: K? = null
)