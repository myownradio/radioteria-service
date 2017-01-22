package com.radioteria.services.user

import com.radioteria.db.entities.User

interface PasswordRecoveryService {
    fun sendPasswordRecoveryLetter(user: User)
    fun verifyPasswordRecoveryCode(code: String)
    fun changePasswordUsingRecoveryCode(code: String, newPassword: String)
}
