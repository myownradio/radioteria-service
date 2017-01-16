package com.radioteria.db.tests.repositories

import com.radioteria.db.entities.Content
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

    @Before
    fun seed() {
        LongRange(0, 4)
                .map { Content(hash = "hash-$it") }
                .apply { forEach { contentRepo.persist(it) } }
    }

    @Test
    @Transactional
    open fun findRandomById() {
        val stored = contentRepo.list()
        val randomIndex = Random().nextInt(stored.size)
        val randomItem = stored[randomIndex]

        val foundItem = contentRepo.find(randomItem.id as Long)

        assertEquals(randomItem, foundItem)
    }

    @Test
    @Transactional
    open fun list() {
        assertEquals(5, contentRepo.list().size)
    }

    @Test
    @Transactional
    open fun listByQuery() {

    }

}