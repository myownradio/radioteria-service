package com.radioteria.player.broadcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChannelOutputStream extends OutputStream {
    final private static Logger LOGGER = LoggerFactory.getLogger(ChannelOutputStream.class);
    final private ConcurrentLinkedQueue<OutputStream> listeners = new ConcurrentLinkedQueue<>();

    private volatile long bytesWritten = 0L;

    @FunctionalInterface
    private interface ListenerConsumer {
        void accept(OutputStream outputStream) throws IOException;
    }

    @Override
    public void write(int b) {
        doWithAllListeners(listener -> listener.write(b));
        bytesWritten ++;
    }

    @Override
    public void write(byte[] b, int off, int len) {
        doWithAllListeners(listener -> listener.write(b, off, len));
        bytesWritten += len;
    }

    @Override
    public void write(byte[] b) {
        doWithAllListeners(listener -> listener.write(b));
        bytesWritten += b.length;
    }

    @Override
    public void flush() {
        doWithAllListeners(OutputStream::flush);
    }

    @Override
    public void close() {
        doWithAllListeners(OutputStream::close);
    }

    public void addListener(OutputStream listener) {
        LOGGER.info("Adding new listener {} to {}.", listener, this);
        listeners.add(listener);
    }

    public void closeAndClear() {
        close();
        clear();
    }

    private void doWithAllListeners(ListenerConsumer consumer) {
        List<OutputStream> queueToRemove = new ArrayList<>();
        for (OutputStream listener : listeners) {
            try {
                consumer.accept(listener);
            } catch (IOException e) {
                LOGGER.error("Listener {} threw an error. Marked as to be removed.", listener);
                queueToRemove.add(listener);
            }
        }
        removeListeners(queueToRemove);
    }

    private void removeListeners(List<OutputStream> queueToRemove) {
        if (!queueToRemove.isEmpty()) {
            LOGGER.info("Removing listeners {} from {}.", queueToRemove, this);
            listeners.removeAll(queueToRemove);
        }
    }

    private void clear() {
        listeners.clear();
    }

    public long getBytesWritten() {
        return bytesWritten;
    }
}
