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

@RunWith(MockitoJUnitRunner.class)
public class NonBlockingServletOutputStreamTest {

    @Mock
    private ServletOutputStream sos;

    private NonBlockingServletOutputStream nbsos;

    @Before
    public void setup() {
        nbsos = new NonBlockingServletOutputStream(sos);
    }

    private byte[] getTestData() {
        return new byte[] { 0x00, 0x00, 0x00, 0x00 };
    }

    @Test
    public void testWhenReady() throws IOException {
        when(sos.isReady()).thenReturn(true);

        byte[] testData = getTestData();

        nbsos.write(testData);
        nbsos.write(testData, 1, 2);
        nbsos.write((byte) 64);

        verify(sos, times(3)).isReady();

        verify(sos, times(1)).write(testData);
        verify(sos, times(1)).write(testData, 1, 2);
        verify(sos, times(1)).write((byte) 64);
    }

    @Test(expected = IOException.class)
    public void testWhenNotReady() throws IOException {
        when(sos.isReady()).thenReturn(false);

        nbsos.write(getTestData());
    }

    @Test(expected = IOException.class)
    public void testWhenReadyAndThenNotReady() throws IOException {
        when(sos.isReady()).thenReturn(true).thenReturn(false);

        try {
            nbsos.write(getTestData());
        } catch (IOException e) {
            Assert.fail("Unexpected IOException.");
        }

        nbsos.write(getTestData());
    }

    @Test
    public void testFlush() throws IOException {
        nbsos.flush();

        verify(sos, times(1)).flush();
    }

    @Test
    public void testCancel() throws IOException {
        nbsos.close();

        verify(sos, times(1)).close();
    }
}
