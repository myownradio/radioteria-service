package com.radioteria.test.services.user

import com.radioteria.db.enums.UserState
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

open class UserActivationTest : BaseUserServiceTest() {

    @Before
    fun before() {
        userService.register("foo@bar.com", "", "")
    }

    @Test
    @Transactional
    open fun activateUser() {

        userService.activateByEmail("foo@bar.com")

        val activatedUser = userService.findByEmail("foo@bar.com")

        if (activatedUser == null) {
            Assert.fail()
            return
        }

        assertEquals(UserState.ACTIVE, activatedUser.state)

    }

}
