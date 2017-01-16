package com.radioteria.db.repositories

import com.radioteria.db.entities.Channel
import org.springframework.stereotype.Repository

@Repository
class JpaChannelRepository : JpaEntityRepository<Long, Channel>(Channel::class.java, Long::class.java), ChannelRepository