package com.radioteria.player.broadcast;

import com.radioteria.util.io.IOConsumer;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NonBlockingServletOutputStream extends OutputStream {
    private ServletOutputStream outputStream;

    public NonBlockingServletOutputStream(ServletOutputStream servletOutputStream) {
        outputStream = servletOutputStream;
    }

    @Override
    public void write(byte[] b) throws IOException {
        doIfReadyOrThrow(os -> os.write(b));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        doIfReadyOrThrow(os -> os.write(b, off, len));
    }

    @Override
    public void write(int b) throws IOException {
        doIfReadyOrThrow(os -> os.write(b));
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    private void doIfReadyOrThrow(IOConsumer<ServletOutputStream> consumer) throws IOException {
        if (outputStream.isReady()) {
            consumer.accept(outputStream);
            return;
        }

        close();

        throw new IOException("Output stream is not ready.");
    }
}
