package com.radioteria.test.services.user

import com.radioteria.services.user.PasswordRecoveryService
import com.radioteria.services.util.TestEventPublisher
import com.radioteria.support.services.mail.EmailSentEvent
import org.junit.Ignore
import org.junit.Test
import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource
import kotlin.test.assertTrue

open class PasswordRecoveryTest : BaseUserServiceTest() {

    @Resource
    lateinit var passwordRecoveryService: PasswordRecoveryService

    @Ignore
    @Test
    @Transactional
    open fun sendRecoveryLetter() {
        userService.register("foo@bar.com", "forgotten-password", "Foo")

        val user = userRepository.findByEmail("foo@bar.com")!!

        passwordRecoveryService.sendPasswordRecoveryLetter(user)

        assertTrue {
            (eventPublisher as TestEventPublisher)
                .isEventPublished(EmailSentEvent::class.java)
        }
    }
}