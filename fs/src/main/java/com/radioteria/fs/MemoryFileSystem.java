package com.radioteria.fs;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.io.IOUtils.copy;

public class MemoryFileSystem implements FileSystem {
    private static class FileEntry {
        private String type;
        private byte[] content;

        public FileEntry(String type, byte[] content) {
            this.type = type;
            this.content = content;
        }
    }

    private Map<String, FileEntry> fileSystem = new HashMap<>();

    private FileEntry getEntryOrError(String path) throws FileSystemException {
        if (!has(path)) {
            throw new FileSystemException(String.format("File %s not found.", path));
        }
        return fileSystem.get(path);
    }

    @Override
    public boolean has(String path) throws FileSystemException {
        return fileSystem.containsKey(path);
    }

    @Override
    public InputStream get(String path) throws FileSystemException {
        return new ByteArrayInputStream(getEntryOrError(path).content);
    }

    @Override
    public String getContentType(String path) throws FileSystemException {
        return getEntryOrError(path).type;
    }

    @Override
    public void put(String path, InputStream sourceStream, String contentType) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(sourceStream, baos);
        fileSystem.put(path, new FileEntry(contentType, baos.toByteArray()));
    }

    @Override
    public void delete(String path) throws FileSystemException {
        fileSystem.remove(path);
    }
}
