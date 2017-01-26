package com.radioteria.db.repositories

import com.radioteria.db.entities.User

interface UserRepository : EntityRepository<Long, User> {
    fun findByEmail(email: String): User?
    fun isEmailAvailable(email: String): Boolean
}