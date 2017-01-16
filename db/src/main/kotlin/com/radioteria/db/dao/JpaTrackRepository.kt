package com.radioteria.db.dao

import com.radioteria.db.entities.Track
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class JpaTrackRepository(entityManager: EntityManager) :
        JpaEntityRepository<Long, Track>(Track::class.java, Long::class.java, entityManager),
        TrackRepository