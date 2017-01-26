package com.radioteria.services.user

import com.radioteria.db.entities.User
import org.springframework.security.authentication.BadCredentialsException

interface AuthenticationService {
    fun login(login: String, password: String)
    fun login(user: User)
    fun logout()
}
