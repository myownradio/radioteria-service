package com.radioteria.db.tests.repositories

import com.radioteria.db.entities.Content
import com.radioteria.db.entities.File
import com.radioteria.db.repositories.ContentRepository
import com.radioteria.db.repositories.FileRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource
import kotlin.test.assertEquals

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = arrayOf("classpath:db-context.xml"))
@ActiveProfiles("test")
open class FileRepositoryTest {

    @Resource
    lateinit var fileRepo: FileRepository

    @Resource
    lateinit var contentRepo: ContentRepository

    val content: Content by lazy { makeAndPersistCommonContent() }


    private fun makeAndPersistCommonContent(): Content {
        val commonContent = Content(hash = "content-hash")
        contentRepo.persist(commonContent)
        return commonContent
    }

    @Test
    @Transactional
    open fun findWithSameContent() {
        val oneFile = File(content)
        val twoFile = File(content)

        fileRepo.persist(oneFile)
        fileRepo.persist(twoFile)

        val allWithSameContent = fileRepo.listAllWithSameContent(oneFile)
        val othersWithSameContent = fileRepo.listOthersWithSameContent(oneFile)

        assertEquals(2, allWithSameContent.size)
        assertEquals(1, othersWithSameContent.size)
    }

}