package com.radioteria.services.channel.events

import com.radioteria.db.entities.Channel
import com.radioteria.db.entities.Track
import org.springframework.context.ApplicationEvent

class TrackAddedEvent(source: Any, val track: Track, val channel: Channel) : ApplicationEvent(source)