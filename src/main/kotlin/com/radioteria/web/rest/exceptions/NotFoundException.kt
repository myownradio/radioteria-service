package com.radioteria.web.rest.exceptions

import org.springframework.http.HttpStatus

class NotFoundException(entityName: String, entityId: Any)
    : RestControllerException("Entity \"$entityName\" with id \"$entityId\" not found.", HttpStatus.NOT_FOUND)
