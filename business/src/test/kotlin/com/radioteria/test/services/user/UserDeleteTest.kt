package com.radioteria.test.services.user

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.springframework.transaction.annotation.Transactional


open class UserDeleteTest : BaseUserServiceTest() {

    @Before
    fun before() {
        userService.register("foo@bar.com", "", "")
    }

    @Test
    @Transactional
    open fun deleteExistentUser() {

        val userToDelete = userService.findByEmail("foo@bar.com")

        if (userToDelete == null) {
            Assert.fail()
            return
        }

        userService.delete(userToDelete)

    }

}
