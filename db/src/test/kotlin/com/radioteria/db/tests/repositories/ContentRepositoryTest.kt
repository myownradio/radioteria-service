package com.radioteria.db.tests.repositories

import com.radioteria.db.entities.Content
import com.radioteria.db.entities.ContentMeta
import com.radioteria.db.repositories.ContentRepository
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.annotation.Resource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = arrayOf("classpath:db-context.xml"))
@ActiveProfiles("test")
open class ContentRepositoryTest {

    @Resource
    lateinit var contentRepo: ContentRepository

    @Test
    @Transactional
    open fun findById() {
        val item = Content()

        contentRepo.persist(item)

        val found = contentRepo.find(item.id as Long)

        assertEquals(item, found)
    }

    @Test
    @Transactional
    open fun list() {
        val item = Content()

        contentRepo.persist(item)

        val list = contentRepo.list()

        assertEquals(1, list.size)
        assertEquals(item, list[0])
    }

    @Test
    @Transactional
    open fun findByProperty() {
        val item = Content(hash = "my-hash")

        contentRepo.persist(item)

        val found = contentRepo.find(ContentMeta.HASH, "my-hash")

        assertEquals(item, found)
    }

    open fun delete() {

    }

}