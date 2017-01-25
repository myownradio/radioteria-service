package com.radioteria.services.util

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Primary
@Profile("test")
@Service
class TestEventPublisher : ApplicationEventPublisher {
    private val publishedEvents: MutableList<Any> = arrayListOf()

    override fun publishEvent(event: ApplicationEvent?) {
        publishedEvents.add(event!!)
    }

    override fun publishEvent(event: Any?) {
        publishedEvents.add(event!!)
    }

    fun <T> isEventPublished(eventClass: Class<T>): Boolean {
        return publishedEvents.filterIsInstance(eventClass).isNotEmpty()
    }

    fun <T> isEventPublished(eventClass: Class<T>, times: Int): Boolean {
        return publishedEvents.filterIsInstance(eventClass).size == times
    }
}
