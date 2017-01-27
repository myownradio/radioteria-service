package com.radioteria.web.rest.exceptions

import org.springframework.http.HttpStatus

open class RestControllerException(message: String, val status: HttpStatus) : Exception(message)