package com.radioteria.db.tests.repositories

import com.radioteria.db.entities.Content
import com.radioteria.db.repositories.ContentRepository
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
open class ContentRepositoryTest {

    @Resource
    lateinit var contentRepo: ContentRepository

    @Transactional
    @Test
    open fun createUser() {
        val content = Content()
        contentRepo.persist(content)

        val contents = contentRepo.list()

        assertEquals(1, contents.size)
        assertEquals(content, contents[0])
    }

}