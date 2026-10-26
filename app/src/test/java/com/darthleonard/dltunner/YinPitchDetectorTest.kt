package com.darthleonard.dltunner

import com.darthleonard.dltunner.core.detection.YinPitchDetector
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Random
import kotlin.math.PI
import kotlin.math.sin

class YinPitchDetectorTest {

    private val detector = YinPitchDetector()

    @Test
    fun `detects synthetic sine wave pitch accurately`() = runBlocking {
        val sampleRate = 44100
        val targetFreq = 110.0 // A2 string frequency
        val bufferSize = 4096
        val samples = FloatArray(bufferSize)

        for (i in 0 until bufferSize) {
            samples[i] = (0.8 * sin(2.0 * Math.PI * targetFreq * i / sampleRate)).toFloat()
        }

        val result = detector.detect(samples, sampleRate)

        assertTrue(result.isPitchDetected)
        assertEquals(targetFreq, result.frequency, 1.5)
        assertTrue("Confidence should be high for pure sine wave", result.confidence > 0.8)
    }

    @Test
    fun `rejects silence and low energy buffer`() = runBlocking {
        val samples = FloatArray(4096) { 0.001f }
        val result = detector.detect(samples, sampleRate = 44100)

        assertFalse(result.isPitchDetected)
        assertEquals(0.0, result.frequency, 1e-4)
    }

    @Test
    fun `detects guitar pitch with DC offset and noise`() = runBlocking {
        val sampleRate = 44100
        val targetFreq = 110.0
        val random = Random(7)
        val samples = FloatArray(4096) { i ->
            val time = i.toDouble() / sampleRate
            (
                0.45 * sin(2.0 * PI * targetFreq * time) +
                    0.35 * sin(2.0 * PI * 6000.0 * time) +
                    0.015 * (random.nextDouble() * 2.0 - 1.0) +
                    0.1
                ).toFloat()
        }

        val result = detector.detect(samples, sampleRate)

        assertTrue(result.isPitchDetected)
        assertEquals(targetFreq, result.frequency, 2.0)
    }

    @Test
    fun `rejects broadband noise without a pitched signal`() = runBlocking {
        val random = Random(11)
        val samples = FloatArray(4096) { (random.nextDouble() * 0.4 - 0.2).toFloat() }

        val result = detector.detect(samples, sampleRate = 44100)

        assertFalse(result.isPitchDetected)
    }
}
