package com.radioteria.web.controllers

import com.radioteria.services.user.UserService
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping

import javax.annotation.Resource
import javax.servlet.http.HttpServletResponse
import java.io.IOException

@Controller
open class BaseController {
    @Resource
    lateinit var userService: UserService

    @RequestMapping("/")
    @Throws(IOException::class)
    fun index(response: HttpServletResponse) {
        response.setHeader("Content-Type", "text/plain")
        response.writer.print("Hello, World!")
    }

    @RequestMapping("views")
    @Secured(value = "ROLE_REGULAR_USER")
    fun view(modelMap: ModelMap): String {
        val authentication = SecurityContextHolder.getContext().authentication
        modelMap.put("user", authentication.name)
        return "index"
    }
}
