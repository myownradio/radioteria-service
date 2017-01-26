package com.radioteria.test.fs

import com.radioteria.fs.FileSystem
import com.radioteria.fs.LocalFileSystem
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import java.io.*

import junit.framework.TestCase.assertFalse
import org.apache.commons.io.IOUtils.copy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class LocalFileSystemTest {
    lateinit var fileSystem: FileSystem

    @Rule
    @JvmField
    val temporaryFolder = TemporaryFolder()

    @Before
    fun before() {
        copy("Hello, World!".byteInputStream(), FileOutputStream(temporaryFolder.newFile("test.txt")))
        copy("Foo!".byteInputStream(), FileOutputStream(temporaryFolder.newFile("foo.txt")))

        fileSystem = LocalFileSystem(temporaryFolder.root.toString())
    }

    @Test
    fun testFileDoesNotExist() {
        assertFalse(fileSystem.has("other.txt"))
    }

    @Test
    fun testFileExists() {
        assertTrue(fileSystem.has("test.txt"))
        assertTrue(fileSystem.has("foo.txt"))
    }

    @Test
    fun testReadFile() {
        assertEquals("Hello, World!", getStreamContent(fileSystem.get("test.txt")))
        assertEquals("Foo!", getStreamContent(fileSystem.get("foo.txt")))
    }

    @Test
    fun testWriteFile() {
        val stream = contentToStream("Something!")
        fileSystem.put("some.txt", stream, "content/type")

        assertTrue(fileSystem.has("some.txt"))
        assertEquals("Something!", getStreamContent(fileSystem.get("some.txt")))
    }

    private fun getStreamContent(stream: InputStream): String {
        val stringWriter = StringWriter()
        copy(stream, stringWriter)
        return stringWriter.toString()
    }

    private fun contentToStream(content: String): InputStream {
        return ByteArrayInputStream(content.toByteArray())
    }
}
