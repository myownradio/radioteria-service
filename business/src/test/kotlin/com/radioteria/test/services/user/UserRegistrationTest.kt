package com.radioteria.test.services.user

import com.radioteria.services.user.events.UserRegisteredEvent
import com.radioteria.services.user.exceptions.UserServiceException
import com.radioteria.services.util.TestEventPublisher
import org.junit.Test

import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertTrue

open class UserRegistrationTest : BaseUserServiceTest() {

    @Test
    @Transactional
    open fun registerUser() {
        userService.register("foo@bar.com", "baz", "Foo Bar")

        assertTrue { userService.exists("foo@bar.com") }

        assertTrue { (eventPublisher as TestEventPublisher)
                .isEventPublished(UserRegisteredEvent::class.java) }
    }

    @Test(expected = UserServiceException::class)
    @Transactional
    open fun testUserRegistrationWhenUserExists() {
        userService.register("foo@bar.com", "baz", "Foo Bar")
        userService.register("foo@bar.com", "baz", "Foo Bar")
    }

}
