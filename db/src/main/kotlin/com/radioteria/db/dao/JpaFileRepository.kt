package com.radioteria.db.dao

import com.radioteria.db.entities.File
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class JpaFileRepository(entityManager: EntityManager) :
        JpaEntityRepository<Long, File>(File::class.java, Long::class.java, entityManager), FileRepository