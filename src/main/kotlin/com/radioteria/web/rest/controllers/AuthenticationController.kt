package com.radioteria.web.rest.controllers

import com.radioteria.services.user.AuthenticationService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
class AuthenticationController : BaseRestController() {
    @Resource
    lateinit var authenticationManager: AuthenticationManager

    fun login() {

    }
}