package com.radioteria.web.forms

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class CreateChannelForm() {
    @field:NotNull
    @field:Size(min = 1, max = 32, message = "Channel size must contain at least 1 character.")
    lateinit var name: String
}