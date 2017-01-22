package com.radioteria.services.channel.exceptions

import com.radioteria.services.exceptions.ServiceException

class ChannelControlServiceException : ServiceException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)

}