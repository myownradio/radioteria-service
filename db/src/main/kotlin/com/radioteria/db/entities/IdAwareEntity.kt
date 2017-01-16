package com.radioteria.db.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id

const val ID_FIELD = "id"

abstract class IdAwareEntity<K : Serializable>(
        @Id
        @GeneratedValue
        @Column(name = ID_FIELD)
        var id: K? = null
)