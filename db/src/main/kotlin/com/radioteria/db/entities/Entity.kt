package com.radioteria.db.entities

import java.io.Serializable

abstract class Entity<out K : Serializable> : IdAware<K> {

}