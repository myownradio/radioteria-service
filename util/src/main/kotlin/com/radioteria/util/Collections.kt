package com.radioteria.util

import java.util.*

fun <T> Iterable<T>.shuffled(): List<T> {
    val random = Random()
    return sortedBy { random.nextInt(100) }
}
