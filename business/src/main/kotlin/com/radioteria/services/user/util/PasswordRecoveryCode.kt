package com.radioteria.services.user.util

import com.radioteria.db.entities.User
import org.springframework.util.DigestUtils

import java.util.Base64

data class PasswordRecoveryCode(val userEmail: String, val userDigest: String, val codeStaleTime: Long) {

    constructor(user: User, codeStaleTime: Long) : this(user.email, generateUserDigest(user), codeStaleTime) {}

    fun encode(): String {
        val encoder = Base64.getEncoder()
        val codeParts = arrayOf(userEmail, userDigest, java.lang.Long.toString(codeStaleTime))
        return encoder.encodeToString(codeParts.joinToString(CODE_DELIMITER).toByteArray())
    }

    fun digestMatches(user: User): Boolean {
        return userDigest == generateUserDigest(user)
    }

    companion object {

        val CODE_DELIMITER = ":"

        fun decode(encodedCode: String): PasswordRecoveryCode {
            val decodedCode = decodePasswordRecoveryCode(encodedCode)
            val codeParts = decodedCode.split(CODE_DELIMITER.toRegex(), 3).toTypedArray()

            if (codeParts.size != 3) {
                throw IllegalArgumentException("Specified code is not valid.")
            }

            val userEmail = codeParts[0]
            val userDigest = codeParts[1]
            val codeStaleTime = java.lang.Long.parseLong(codeParts[2])

            return PasswordRecoveryCode(userEmail, userDigest, codeStaleTime)
        }

        private fun decodePasswordRecoveryCode(code: String): String {
            val decoder = Base64.getDecoder()
            return String(decoder.decode(code))
        }

        private fun generateUserDigest(user: User): String {
            val userCredentials = user.email + user.password
            return DigestUtils.md5DigestAsHex(userCredentials.toByteArray())
        }
    }
}
