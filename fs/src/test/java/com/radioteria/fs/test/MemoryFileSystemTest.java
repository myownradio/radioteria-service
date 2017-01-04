package com.radioteria.fs.test;

import com.radioteria.fs.FileSystem;
import com.radioteria.fs.FileSystemException;
import com.radioteria.fs.MemoryFileSystem;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static junit.framework.TestCase.assertFalse;
import static org.apache.commons.io.IOUtils.copy;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MemoryFileSystemTest {
    private FileSystem fileSystem = new MemoryFileSystem();

    @Test
    public void testFileDoesNotExist() throws FileSystemException {
        assertFalse(fileSystem.has("foo"));
    }

    @Test
    public void testPutGetFile() throws IOException {
        byte[] testData = "Hello, World!".getBytes();
        String contentType = "application/octet-stream";

        fileSystem.put("foo", new ByteArrayInputStream(testData), contentType);

        ByteArrayOutputStream content = new ByteArrayOutputStream();

        copy(fileSystem.get("foo"), content);

        assertArrayEquals(testData, content.toByteArray());
        assertEquals(contentType, fileSystem.getContentType("foo"));
    }

    @Test
    public void testFileExists() throws IOException {
        byte[] testData = "Hello, World!".getBytes();

        fileSystem.put("foo", new ByteArrayInputStream(testData), "application/octet-stream");

        assertTrue(fileSystem.has("foo"));
    }
}
