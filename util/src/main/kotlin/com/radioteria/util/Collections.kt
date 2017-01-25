package com.radioteria.util

import java.util.*

fun <T> Iterable<T>.shuffled(): List<T> {
    val random = Random()
    return sortedBy { random.nextInt(100) }
}

fun sortedUntil(from: Int, to: Int): IntRange {
    return if (from <= to) from..to else to..from
}