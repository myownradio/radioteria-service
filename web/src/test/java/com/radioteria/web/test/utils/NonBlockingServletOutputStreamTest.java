package com.radioteria.web.test.utils;

import com.radioteria.utils.NonBlockingServletOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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

        Mockito.verify(sos, Mockito.times(3)).isReady();

        Mockito.verify(sos, Mockito.times(1)).write(testData);
        Mockito.verify(sos, Mockito.times(1)).write(testData, 1, 2);
        Mockito.verify(sos, Mockito.times(1)).write((byte) 64);
    }

    @Test
    public void testWhenNotReady() throws IOException {
        Mockito.when(sos.isReady()).thenReturn(false);

        byte[] testData = getTestData();

        try {
            nbsos.write(testData);
            Assert.fail("Expected IOException.");
        } catch (IOException e) {
            /* NOP */
        }

        Mockito.verify(sos, Mockito.times(1)).isReady();
        Mockito.verify(sos, Mockito.times(0)).write(testData);
    }

    @Test
    public void testWhenReadyAndThenNotReady() throws IOException {
        Mockito.when(sos.isReady())
                .thenReturn(true)
                .thenReturn(false);

        byte[] testData = getTestData();

        try {
            nbsos.write(testData);
        } catch (IOException e) {
            Assert.fail("Unexpected IOException.");
        }

        try {
            nbsos.write(testData);
            Assert.fail("Expected IOException.");
        } catch (IOException e) {
            /* NOP */
        }

        Mockito.verify(sos, Mockito.times(2)).isReady();
        Mockito.verify(sos, Mockito.times(1)).write(testData);
    }

    @Test
    public void testFlush() throws IOException {
        nbsos.flush();

        Mockito.verify(sos, Mockito.times(1)).flush();
    }

    @Test
    public void testCancel() throws IOException {
        nbsos.close();

        Mockito.verify(sos, Mockito.times(1)).close();
    }
}
