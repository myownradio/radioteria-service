package com.radioteria.db.repositories

import com.radioteria.db.entities.Content
import org.springframework.stereotype.Repository

@Repository
class JpaContentRepository : JpaEntityRepository<Long, Content>(Content::class.java, Long::class.java), ContentRepository {
    override fun hashExists(hash: String): Boolean {
        return findByPropertyValue("hash", hash) != null
    }
}