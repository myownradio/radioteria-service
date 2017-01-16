package com.radioteria.db.dao

import com.radioteria.db.entities.Channel
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class JpaChannelRepository(entityManager: EntityManager) :
        JpaEntityRepository<Long, Channel>(Channel::class.java, Long::class.java, entityManager), ChannelRepository