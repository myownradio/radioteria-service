package com.radioteria.db.dao

import com.radioteria.db.entities.Content
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class JpaContentRepository(entityManager: EntityManager) :
    JpaEntityRepository<Long, Content>(Content::class.java, Long::class.java, entityManager), ContentRepository