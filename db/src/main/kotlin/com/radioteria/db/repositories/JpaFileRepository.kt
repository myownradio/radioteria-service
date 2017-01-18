package com.radioteria.db.repositories

import com.radioteria.db.entities.File
import org.springframework.stereotype.Repository

@Repository
class JpaFileRepository : JpaEntityRepository<Long, File>(File::class.java, Long::class.java), FileRepository {
    override fun listAllWithSameContent(file: File): List<File> {
        return listByPropertyValue("content", file.content)
    }
    override fun listOthersWithSameContent(file: File): List<File> {
        return listAllWithSameContent(file).filterNot { it == file }
    }
}