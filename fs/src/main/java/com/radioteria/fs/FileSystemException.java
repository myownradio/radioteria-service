package com.radioteria.fs;

import java.io.IOException;

public class FileSystemException extends IOException {
    public FileSystemException() {
        super();
    }

    public FileSystemException(String message) {
        super(message);
    }

    public FileSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSystemException(Throwable cause) {
        super(cause);
    }
}
