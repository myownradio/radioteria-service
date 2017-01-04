package com.radioteria.fs;

import java.io.IOException;
import java.io.InputStream;

public interface FileSystem {
    boolean has(String path) throws FileSystemException;
    InputStream get(String path) throws FileSystemException;
    String getContentType(String path) throws FileSystemException;
    void put(String path, InputStream sourceStream, String contentType) throws IOException;
    void delete(String path) throws FileSystemException;
}
