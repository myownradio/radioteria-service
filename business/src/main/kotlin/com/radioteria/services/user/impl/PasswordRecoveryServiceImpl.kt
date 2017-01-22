package com.radioteria.services.user.impl

import com.radioteria.services.user.util.PasswordRecoveryCode
import com.radioteria.services.user.UserService
import com.radioteria.support.template.TemplateService
import com.radioteria.services.user.PasswordRecoveryService
import com.radioteria.db.entities.User
import com.radioteria.db.enums.UserState
import com.radioteria.db.repositories.UserRepository
import com.radioteria.services.user.exceptions.UserServiceException
import com.radioteria.services.util.TimeService
import com.radioteria.support.services.mail.EmailService
import org.springframework.stereotype.Service

import java.util.concurrent.TimeUnit

@Service
class PasswordRecoveryServiceImpl(private val userDao: UserRepository,
                                  private val emailService: EmailService,
                                  private val templateService: TemplateService,
                                  private val userService: UserService,
                                  private val timeService: TimeService
) : PasswordRecoveryService {

    companion object {
        val PASSWORD_RECOVERY_LETTER_SUBJECT = "Radioteria Password Recovery"
        val PASSWORD_RECOVERY_LETTER_TEMPLATE = "email.password-recovery"
        val PASSWORD_RECOVERY_CODE_TTL: Long = TimeUnit.MINUTES.toMillis(30)
    }

    override fun sendPasswordRecoveryLetter(user: User) {
        if (user.state == UserState.DELETED) {
            throw UserServiceException("User is deleted.")
        }

        val recoveryCode = getEncodedRecoveryCode(user)
        val body = renderRecoveryLetter(user, recoveryCode)

        sendLetterToUser(user, body)
    }

    private fun getEncodedRecoveryCode(user: User): String {
        val codeStaleTime = makeRecoveryCodeStaleTime()
        val recoveryCode = PasswordRecoveryCode(user, codeStaleTime)

        return recoveryCode.encode()
    }

    private fun makeRecoveryCodeStaleTime(): Long {
        return timeService.getTimeMillis() + PASSWORD_RECOVERY_CODE_TTL
    }

    private fun renderRecoveryLetter(user: User, recoveryCode: String): String {
        val context = mapOf("user" to user, "code" to recoveryCode)
        return templateService.render(PASSWORD_RECOVERY_LETTER_TEMPLATE, context)
    }

    private fun sendLetterToUser(user: User, body: String) {
        emailService.send(user.email, PASSWORD_RECOVERY_LETTER_SUBJECT, body)
    }

    override fun verifyPasswordRecoveryCode(code: String) {
        try {
            val recoveryCode = verifyAndDecodeRecoveryCode(code)
            val user = getUserFromRecoveryCode(recoveryCode)

            failIfCodeNotCorrespondToTheUser(recoveryCode, user)
        } catch (e: IllegalArgumentException) {
            throw UserServiceException("Specified code broken.", e)
        }

    }

    override fun changePasswordUsingRecoveryCode(code: String, newPassword: String) {
        try {
            val recoveryCode = verifyAndDecodeRecoveryCode(code)
            val user = getUserFromRecoveryCode(recoveryCode)

            failIfCodeNotCorrespondToTheUser(recoveryCode, user)

            userService.changePassword(user, newPassword)
        } catch (e: IllegalArgumentException) {
            throw UserServiceException("Specified code broken.", e)
        }

    }

    private fun getUserFromRecoveryCode(recoveryCode: PasswordRecoveryCode): User {
        return userDao.findByEmail(recoveryCode.userEmail)
                ?: throw UserServiceException("Specified code belongs to user that does not exist.")
    }

    private fun failIfCodeNotCorrespondToTheUser(recoveryCode: PasswordRecoveryCode, user: User) {
        if (user.state == UserState.DELETED) {
            throw UserServiceException("Specified code belongs to user that was deleted.")
        }
        if (!recoveryCode.digestMatches(user)) {
            throw UserServiceException("Code verification failed.")
        }
    }

    private fun verifyAndDecodeRecoveryCode(encodedCode: String): PasswordRecoveryCode {
        val recoveryCode = PasswordRecoveryCode.decode(encodedCode)
        if (isRecoveryCodeStale(recoveryCode)) {
            throw UserServiceException("Specified code is stale.")
        }
        return recoveryCode
    }

    private fun isRecoveryCodeStale(recoveryCode: PasswordRecoveryCode): Boolean {
        return recoveryCode.codeStaleTime < timeService.getTimeMillis()
    }

}
