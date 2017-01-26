package com.radioteria.db.repositories

import com.radioteria.db.entities.File

interface FileRepository : EntityRepository<Long, File> {
    fun listAllWithSameContent(file: File): List<File>
    fun listOthersWithSameContent(file: File): List<File>
}