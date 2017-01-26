package com.radioteria.test.services.user

import org.junit.Before
import org.junit.Test
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertTrue

open class UserChangePasswordTest : BaseUserServiceTest() {

    @Before
    fun before() {
        userService.register("foo@bar.com", "old-password", "")
    }

    @Test
    @Transactional
    open fun changePassword() {
        val user = userRepository.findByEmail("foo@bar.com")!!

        userService.changePassword(user, "new-password")

        assertTrue { userService.passwordMatch("new-password", user) }
    }

}