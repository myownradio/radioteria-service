package com.radioteria.services.util

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher

class TestEventPublisher : ApplicationEventPublisher {
    private val publishedEvents: MutableList<Any> = arrayListOf()

    override fun publishEvent(event: ApplicationEvent?) {
        publishedEvents.add(event!!)
    }

    override fun publishEvent(event: Any?) {
        publishedEvents.add(event!!)
    }

    fun <T> hasPublished(eventClass: Class<T>): Boolean {
        return publishedEvents.filterIsInstance(eventClass).isNotEmpty()
    }
}
