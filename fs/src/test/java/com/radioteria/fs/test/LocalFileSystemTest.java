package com.radioteria.fs.test;

import com.radioteria.fs.FileSystem;
import com.radioteria.fs.FileSystemException;
import com.radioteria.fs.LocalFileSystem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

import static junit.framework.TestCase.assertFalse;
import static org.apache.commons.io.CopyUtils.copy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocalFileSystemTest {
    private FileSystem fileSystem;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        copy("Hello, World!", new FileOutputStream(temporaryFolder.newFile("test.txt")));
        copy("Foo!", new FileOutputStream(temporaryFolder.newFile("foo.txt")));

        fileSystem = new LocalFileSystem(temporaryFolder.getRoot().toString());
    }

    @Test
    public void testFileDoesNotExist() throws FileSystemException {
        assertFalse(fileSystem.has("other.txt"));
    }

    @Test
    public void testFileExists() throws FileSystemException {
        assertTrue(fileSystem.has("test.txt"));
        assertTrue(fileSystem.has("foo.txt"));
    }

    @Test
    public void testReadFile() throws IOException {
        assertEquals("Hello, World!", getStreamContent(fileSystem.get("test.txt")));
        assertEquals("Foo!", getStreamContent(fileSystem.get("foo.txt")));
    }

    @Test
    public void testWriteFile() throws IOException {
        InputStream stream = contentToStream("Something!");
        fileSystem.put("some.txt", stream, "content/type");

        assertTrue(fileSystem.has("some.txt"));
        assertEquals("Something!", getStreamContent(fileSystem.get("some.txt")));
    }

    private String getStreamContent(InputStream is) throws IOException {
        StringWriter stringWriter = new StringWriter();
        copy(is, stringWriter);
        return stringWriter.toString();
    }

    private InputStream contentToStream(String content) throws IOException {
        return new ByteArrayInputStream(content.getBytes());
    }
}
