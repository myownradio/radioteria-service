package com.radioteria.services.channel.events

import com.radioteria.db.entities.Channel
import org.springframework.context.ApplicationEvent

class ChannelControlsEvent(source: Any, val channel: Channel) : ApplicationEvent(source)
