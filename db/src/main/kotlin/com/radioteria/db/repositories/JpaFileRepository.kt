package com.radioteria.db.repositories

import com.radioteria.db.entities.File
import org.springframework.stereotype.Repository

@Repository
class JpaFileRepository : JpaEntityRepository<Long, File>(File::class.java, Long::class.java), FileRepository