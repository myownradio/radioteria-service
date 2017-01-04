package com.radioteria.player.test;

import com.radioteria.player.broadcast.NonBlockingServletOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.only;

@RunWith(MockitoJUnitRunner.class)
public class NonBlockingServletOutputStreamTest {
    @Mock
    ServletOutputStream sos;

    NonBlockingServletOutputStream nbsos;

    @Before
    public void setup() {
        nbsos = new NonBlockingServletOutputStream(sos);
    }

    @Test
    public void testWhenReady() throws IOException {
        when(sos.isReady()).thenReturn(true);

        byte[] data = { 0x00, 0x00, 0x00, 0x00 };
        nbsos.write(data);

        verify(sos, times(1)).isReady();
        verify(sos, times(1)).write(data);
    }

    @Test(expected = IOException.class)
    public void testWhenNotReady() throws IOException {
        when(sos.isReady()).thenReturn(false);

        byte[] data = { 0x00, 0x00, 0x00, 0x00 };
        nbsos.write(data);
    }

    @Test(expected = IOException.class)
    public void testWhenReadyAndThenNotReady() throws IOException {
        when(sos.isReady()).thenReturn(true).thenReturn(false);

        byte[] data = { 0x00, 0x00, 0x00, 0x00 };
        try {
            nbsos.write(data);
        } catch (IOException e) {
            Assert.fail("Unexpected IOException.");
        }

        nbsos.write(data);
    }
}
