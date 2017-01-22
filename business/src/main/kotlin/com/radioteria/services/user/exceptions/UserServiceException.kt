package com.radioteria.services.user.exceptions

import com.radioteria.services.exceptions.ServiceException

class UserServiceException : ServiceException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)

}