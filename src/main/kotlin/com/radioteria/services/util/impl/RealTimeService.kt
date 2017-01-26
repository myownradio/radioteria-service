package com.radioteria.services.util.impl

import com.radioteria.services.util.TimeService
import org.springframework.stereotype.Service

@Service
class RealTimeService : TimeService {
    override fun getTimeMillis(): Long = System.currentTimeMillis()
}