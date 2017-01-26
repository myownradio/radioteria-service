package com.radioteria.test.services.user

import com.radioteria.db.repositories.UserRepository
import com.radioteria.services.user.UserService
import com.radioteria.test.services.BaseServiceTest
import org.springframework.context.ApplicationEventPublisher
import javax.annotation.Resource

abstract class BaseUserServiceTest : BaseServiceTest() {

    @Resource
    lateinit var userRepository: UserRepository

    @Resource
    lateinit var userService: UserService

    @Resource
    lateinit var eventPublisher: ApplicationEventPublisher

}
