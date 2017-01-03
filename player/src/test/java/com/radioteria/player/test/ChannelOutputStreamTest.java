package com.radioteria.player.test;

import com.radioteria.player.broadcast.ChannelOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
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

        ByteArrayOutputStream listener = new ByteArrayOutputStream(bytesToWrite.length);
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
                .mapToObj(n -> new ByteArrayOutputStream(bytesToWrite.length))
                .toArray(ByteArrayOutputStream[]::new);

        ChannelOutputStream os = new ChannelOutputStream();

        Arrays.stream(listeners).forEach(os::addListener);

        os.write(bytesToWrite);

        assertEquals(bytesToWrite.length, os.getBytesWritten());
        Arrays.stream(listeners).forEach(l -> assertArrayEquals(bytesToWrite, l.toByteArray()));
    }
}
