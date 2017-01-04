package com.radioteria.player.test;

import com.radioteria.player.broadcast.ChannelOutputStream;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class ChannelOutputStreamTest {
    @Test
    public void writeToEmptyStream() {
        byte[] bytesToWrite = new byte[] { 0x00, 0x01, 0x02, 0x03 };

        ChannelOutputStream os = new ChannelOutputStream();
        os.write(bytesToWrite);

        assertEquals(bytesToWrite.length, os.getBytesWritten());
    }

    @Test
    public void writeToOneListener() {
        byte[] bytesToWrite = new byte[] { 0x00, 0x01, 0x02, 0x03 };

        ByteArrayOutputStream listener = new ByteArrayOutputStream();
        ChannelOutputStream os = new ChannelOutputStream();
        os.addListener(listener);

        os.write(bytesToWrite);

        assertEquals(bytesToWrite.length, os.getBytesWritten());
        assertArrayEquals(bytesToWrite, listener.toByteArray());
    }

    @Test
    public void writeToMoreListeners() {
        byte[] bytesToWrite = new byte[] { 0x00, 0x01, 0x02, 0x03 };

        ByteArrayOutputStream[] listeners = IntStream
                .range(0, 16)
                .mapToObj(n -> new ByteArrayOutputStream())
                .toArray(ByteArrayOutputStream[]::new);

        ChannelOutputStream os = new ChannelOutputStream();

        Arrays.stream(listeners).forEach(os::addListener);

        os.write(bytesToWrite);

        assertEquals(bytesToWrite.length, os.getBytesWritten());
        Arrays.stream(listeners)
                .map(ByteArrayOutputStream::toByteArray)
                .forEach(arr -> assertArrayEquals(bytesToWrite, arr));
    }

    @Test
    public void writeIfOneListenerThrowsException() {
        byte[] bytesToWrite = new byte[] { 0x00, 0x01, 0x02, 0x03 };

        ChannelOutputStream os = new ChannelOutputStream();

        ByteArrayOutputStream goodListener = new ByteArrayOutputStream();
        ByteArrayOutputStream badListener = new ByteArrayOutputStream() {
            @Override
            public void write(byte[] b) throws IOException {
                throw new IOException("I am a bad boy!");
            }
        };

        os.addListener(goodListener);
        os.addListener(badListener);

        assertEquals(2L, os.getListenersCount());

        os.write(bytesToWrite);

        assertEquals(1L, os.getListenersCount());
        assertArrayEquals(bytesToWrite, goodListener.toByteArray());
    }
}
