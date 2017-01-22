package com.radioteria.services.util.impl

import com.radioteria.services.util.TimeService

class RealTimeService : TimeService {
    override fun getTimeMillis(): Long = System.currentTimeMillis()
}