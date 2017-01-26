package com.radioteria.services.user.events

import com.radioteria.db.entities.User
import org.springframework.context.ApplicationEvent

class UserRegisteredEvent(source: Any, val user: User) : ApplicationEvent(source)
