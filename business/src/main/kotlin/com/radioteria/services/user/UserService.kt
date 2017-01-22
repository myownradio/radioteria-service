package com.radioteria.services.user

import com.radioteria.db.entities.User

import java.util.Optional

interface UserService {
    fun isPasswordMatch(plainPassword: String, user: User): Boolean
    fun isEmailAvailable(email: String): Boolean

    fun register(email: String, plainPassword: String, name: String)
    fun activateByEmail(email: String)
    fun changePassword(user: User, newPlainPassword: String)

    fun findByEmail(email: String): User?
    fun findById(id: Long): User?

    fun deactivate(user: User)
    fun delete(user: User)
}
