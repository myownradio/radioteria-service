package com.radioteria.test.fs

import com.radioteria.fs.MemoryFileSystem
import org.junit.Test

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

import junit.framework.TestCase.assertFalse
import org.apache.commons.io.IOUtils.copy
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class MemoryFileSystemTest {
    private val fileSystem = MemoryFileSystem()

    @Test
    fun testFileDoesNotExist() {
        assertFalse(fileSystem.has("foo"))
    }

    @Test
    fun testPutGetFile() {
        val testData = "Hello, World!".toByteArray()
        val contentType = "application/octet-stream"

        fileSystem.put("foo", ByteArrayInputStream(testData), contentType)

        val content = ByteArrayOutputStream()

        copy(fileSystem.get("foo"), content)

        assertArrayEquals(testData, content.toByteArray())
        assertEquals(contentType, fileSystem.getContentType("foo"))
    }

    @Test
    fun testFileExists() {
        val testData = "Hello, World!".toByteArray()

        fileSystem.put("foo", ByteArrayInputStream(testData), "application/octet-stream")

        assertTrue(fileSystem.has("foo"))
    }
}
