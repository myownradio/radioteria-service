package com.radioteria.services.user.impl

import com.radioteria.db.repositories.UserRepository
import com.radioteria.services.user.events.PasswordChangedEvent
import com.radioteria.services.user.events.UserConfirmedEvent
import com.radioteria.services.user.events.UserRegisteredEvent
import com.radioteria.services.user.UserService
import com.radioteria.db.entities.User
import com.radioteria.db.enums.UserState
import com.radioteria.services.user.events.UserDeletedEvent
import com.radioteria.services.user.exceptions.UserServiceException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userRepository: UserRepository,
                      private val passwordEncoder: PasswordEncoder,
                      private val eventPublisher: ApplicationEventPublisher
) : UserService {

    @Value("\${registration.email.verify.enabled}")
    var emailVerifyEnabled = true

    override fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override fun findById(id: Long): User? {
        return userRepository.findById(id)
    }

    override fun isPasswordMatch(plainPassword: String, user: User): Boolean {
        return passwordEncoder.matches(plainPassword, user.password)
    }

    override fun isEmailAvailable(email: String): Boolean {
        return userRepository.isEmailAvailable(email)
    }

    override fun register(email: String, plainPassword: String, name: String) {
        throwErrorIfEmailNotAvailable(email)

        val encodedPassword = passwordEncoder.encode(plainPassword)
        val user = User(email = email, password = encodedPassword, name = name,
                state = if (emailVerifyEnabled) UserState.INACTIVE else UserState.ACTIVE)

        userRepository.persist(user)

        eventPublisher.publishEvent(UserRegisteredEvent(this, user))
    }

    override fun activateByEmail(email: String) {
        val user = findByEmail(email)
                ?: throw UserServiceException(String.format("User with email \"%s\" not exists.", email))

        user.state = UserState.ACTIVE

        eventPublisher.publishEvent(UserConfirmedEvent(this, user))
    }

    override fun changePassword(user: User, newPlainPassword: String) {
        val encodedPassword = passwordEncoder.encode(newPlainPassword)

        user.password = encodedPassword

        eventPublisher.publishEvent(PasswordChangedEvent(this, user))
    }

    override fun deactivate(user: User) {
        user.state = UserState.DELETED

        eventPublisher.publishEvent(UserDeletedEvent(this, user))
    }

    override fun delete(user: User) {
        throw IllegalStateException("No, God! Please, No!")
    }

    private fun throwErrorIfEmailNotAvailable(email: String) {
        if (!isEmailAvailable(email)) {
            throw UserServiceException(String.format("User with email \"%s\" already exists.", email))
        }
    }

}
