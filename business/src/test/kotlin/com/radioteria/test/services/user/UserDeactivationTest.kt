package com.radioteria.test.services.user

import com.radioteria.db.enums.UserState
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

open class UserDeactivationTest : BaseUserServiceTest() {

    @Before
    fun before() {
        userService.register("foo@bar.com", "", "")
        userService.activateByEmail("foo@bar.com")
    }

    @Test
    @Transactional
    open fun testUserDeactivation() {

        val userToDeactivate = userService.findByEmail("foo@bar.com")

        if (userToDeactivate == null) {
            Assert.fail()
            return
        }

        userService.deactivate(userToDeactivate)

        assertEquals(UserState.DEACTIVATED, userToDeactivate.state)

    }

}
