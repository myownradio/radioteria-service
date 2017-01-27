package com.radioteria.services.auth

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service("restAuthenticationEntryPoint")
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authException: AuthenticationException) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }
}