package com.radioteria.db.dao

import com.radioteria.db.entities.User

interface UserDao : EntityRepository<Long, User> {
    fun findByEmail(email: String): User?
    fun isEmailAvailable(email: String): Boolean
}