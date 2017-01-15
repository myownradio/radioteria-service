package com.radioteria.db.entities

import java.io.Serializable

interface IdAware<out T : Serializable> {
    fun getId(): T?
}