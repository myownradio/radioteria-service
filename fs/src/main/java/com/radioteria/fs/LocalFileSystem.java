package com.radioteria.fs;

import java.io.*;
import java.nio.file.Files;

import static org.apache.commons.io.IOUtils.copy;

public class LocalFileSystem implements FileSystem {
    private File rootDirFile;

    public LocalFileSystem(String rootDir) throws FileSystemException {
        this(new File(rootDir));
    }

    public LocalFileSystem(File rootDir) throws FileSystemException {
        rootDirFile = rootDir;
        if (!rootDirFile.isDirectory() || !rootDirFile.canExecute()) {
            throw new FileSystemException(String.format("Directory %s is not writable.", rootDir));
        }
    }

    private File getOrError(String path) throws FileSystemException {
        if (!has(path)) {
            throw new FileSystemException(String.format("File %s not found.", path));
        }
        return new File(rootDirFile, path);
    }

    @Override
    public boolean has(String path) throws FileSystemException {
        return new File(rootDirFile, path).exists();
    }

    @Override
    public InputStream get(String path) throws FileSystemException {
        try {
            return new FileInputStream(getOrError(path));
        } catch (FileNotFoundException e) {
            throw new FileSystemException(String.format("Can not read %s.", path), e);
        }
    }

    @Override
    public String getContentType(String path) throws FileSystemException {
        try {
            return Files.probeContentType(getOrError(path).toPath());
        } catch (IOException e) {
            throw new FileSystemException(String.format("Can not read %s.", path), e);
        }
    }

    @Override
    public void put(String path, InputStream sourceStream, String contentType) throws IOException {
        try (OutputStream targetStream = new FileOutputStream(new File(rootDirFile, path))) {
            copy(sourceStream, targetStream);
        }
    }

    @Override
    public void delete(String path) throws FileSystemException {
        new File(rootDirFile, path).delete();
    }

    @Override
    public FileSystemType getType() {
        return FileSystemType.LOCAL;
    }
}
