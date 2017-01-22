package com.radioteria.services.util.impl

import com.radioteria.services.util.TimeService

class StaticTimeService(val staticTime: Long) : TimeService {
    override fun getTimeMillis(): Long = staticTime
}