package com.radioteria.services.util.impl

import com.radioteria.services.util.TimeService
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test")
@Primary
class StaticTimeService : TimeService {
    private val staticTime: Long = System.currentTimeMillis()
    override fun getTimeMillis(): Long = staticTime
}