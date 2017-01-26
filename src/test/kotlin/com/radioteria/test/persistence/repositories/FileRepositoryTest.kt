package com.radioteria.test.persistence.repositories

import com.radioteria.db.entities.Content
import com.radioteria.db.entities.File
import com.radioteria.db.repositories.ContentRepository
import com.radioteria.db.repositories.FileRepository
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource
import kotlin.test.assertTrue

@RunWith(SpringJUnit4ClassRunner::class)
@WebAppConfiguration
@ContextConfiguration(locations = arrayOf("classpath:db-context.xml"))
@ActiveProfiles("test")
open class FileRepositoryTest {

    @Resource
    lateinit var fileRepo: FileRepository

    @Resource
    lateinit var contentRepo: ContentRepository

    @Test
    @Transactional
    open fun findWithSameContent() {
        val commonContent = makeAndPersistCommonContent { it.hash = "content-hash" }

        val oneFile = File(commonContent)
        val twoFile = File(commonContent)

        fileRepo.persist(oneFile)
        fileRepo.persist(twoFile)

        val listWithSameContent = fileRepo.listAllWithSameContent(oneFile)

        assertTrue(listWithSameContent.containsAll(listOf(oneFile, twoFile)))

        assertThat(fileRepo.listOthersWithSameContent(oneFile)[0], equalTo(twoFile))
        assertThat(fileRepo.listOthersWithSameContent(twoFile)[0], equalTo(oneFile))
    }

    private fun makeAndPersistCommonContent(contentConsumer: (Content) -> Unit): Content {
        val content = Content()
        contentConsumer.invoke(content)
        contentRepo.persist(content)
        return content
    }

}