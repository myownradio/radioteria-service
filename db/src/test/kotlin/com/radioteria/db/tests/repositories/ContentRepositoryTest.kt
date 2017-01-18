package com.radioteria.db.tests.repositories

import com.radioteria.db.entities.Content
import com.radioteria.db.entities.ContentMeta
import com.radioteria.db.repositories.ContentRepository
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.annotation.Commit
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = arrayOf("classpath:db-context.xml"))
@ActiveProfiles("test")
open class ContentRepositoryTest {

    @Resource
    lateinit var contentRepo: ContentRepository

    @Test
    @Transactional
    open fun findById() {
        val testContent = Content()

        contentRepo.persist(testContent)

        val foundContent = contentRepo.findById(testContent.id!!)

        assertEquals(testContent, foundContent)
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
        val testContent = Content(hash = "my-hash")

        contentRepo.persist(testContent)

        val foundContent = contentRepo.findByPropertyValue(ContentMeta.HASH, "my-hash")

        assertEquals(testContent, foundContent)
    }

    @Test
    @Transactional
    open fun listByProperty() {
        val testObjects = arrayOf(
                Content(hash = "hash-1000", contentType = "text/plain"),
                Content(hash = "hash-2000", contentType = "text/plain"),
                Content(hash = "hash-3000", contentType = "text/plain"),
                Content(hash = "hash-4000", contentType = "image/jpeg"),
                Content(hash = "hash-5000", contentType = "image/jpeg"),
                Content(hash = "hash-6000", contentType = "audio/aac")
        )

        fun countByPropertyValue(property: String, value: String): Int =
                contentRepo.listByPropertyValue(property, value).size

        testObjects.forEach { contentRepo.persist(it) }

        assertEquals(3, countByPropertyValue("contentType", "text/plain"))
        assertEquals(2, countByPropertyValue("contentType", "image/jpeg"))
        assertEquals(1, countByPropertyValue("contentType", "audio/aac"))
    }

    @Test
    @Transactional
    open fun delete() {
        val testContent = Content(hash = "my-hash")

        contentRepo.persist(testContent)

        assertTrue(contentRepo.list().isNotEmpty())

        contentRepo.remove(testContent)

        assertTrue(contentRepo.list().isEmpty())
    }

}