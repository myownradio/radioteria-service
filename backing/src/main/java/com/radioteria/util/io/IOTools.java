package com.radioteria.util.io;

import java.io.Closeable;
import java.io.IOException;

public class IOTools {
    public static void closeStream(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                /** Nothing to do **/
            }
        }
    }
}
