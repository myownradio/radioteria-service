package com.radioteria.test.util

import com.radioteria.util.io.MultiListenerOutputStream
import org.junit.Test

import java.io.ByteArrayOutputStream
import java.io.IOException

import org.junit.Assert.*

class MultiListenerOutputStreamTest {
    private val testData: ByteArray = byteArrayOf(0x00, 0x01, 0x02, 0x03)

    @Test
    fun writeToEmptyStream() {
        val bytesToWrite = testData

        val os = MultiListenerOutputStream()
        os.write(bytesToWrite)

        assertEquals(bytesToWrite.size.toLong(), os.bytesWritten)
    }

    @Test
    fun writeToOneListener() {
        val bytesToWrite = testData

        val listener = ByteArrayOutputStream()
        val os = MultiListenerOutputStream()
        os.addListener(listener)

        os.write(bytesToWrite)

        assertEquals(bytesToWrite.size.toLong(), os.bytesWritten)
        assertArrayEquals(bytesToWrite, listener.toByteArray())
    }

    @Test
    fun writeToMoreListeners() {
        val bytesToWrite = testData

        val listeners = (0..16)
                .map { ByteArrayOutputStream() }
                .toTypedArray()

        val os = MultiListenerOutputStream()

        listeners.forEach { os.addListener(it) }

        os.write(bytesToWrite)

        assertEquals(bytesToWrite.size.toLong(), os.bytesWritten)

        listeners
                .map { it.toByteArray() }
                .forEach { arr -> assertArrayEquals(bytesToWrite, arr) }
    }

    @Test
    fun writeIfOneListenerThrowsException() {
        val bytesToWrite = testData

        val os = MultiListenerOutputStream()

        val goodListener = ByteArrayOutputStream()
        val badListener = object : ByteArrayOutputStream() {
            @Throws(IOException::class)
            override fun write(b: ByteArray) {
                throw IOException("I am a bad boy!")
            }
        }

        os.addListener(goodListener)
        os.addListener(badListener)

        assertEquals(2L, os.listenersCount)

        os.write(bytesToWrite)

        assertEquals(1L, os.listenersCount)
        assertArrayEquals(bytesToWrite, goodListener.toByteArray())
    }
}
