package com.radioteria.db.repositories

import com.radioteria.db.entities.Track
import org.springframework.stereotype.Repository

@Repository
open class JpaTrackRepository : JpaEntityRepository<Long, Track>(Track::class.java, Long::class.java), TrackRepository