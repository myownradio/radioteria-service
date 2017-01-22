package com.radioteria.services.user.events


import com.radioteria.db.entities.User
import org.springframework.context.ApplicationEvent

class PasswordChangedEvent(source: Any, val user: User) : ApplicationEvent(source)
